package com.deepu.orderprocessingservice.service.serviceImpl;

import com.deepu.orderprocessingservice.constant.Constant;
import com.deepu.orderprocessingservice.entity.Order;
import com.deepu.orderprocessingservice.enumeration.OrderStatus;
import com.deepu.orderprocessingservice.enumeration.ResponseStatus;
import com.deepu.orderprocessingservice.feign.CartServiceClient;
import com.deepu.orderprocessingservice.feign.NotificationServiceClient;
import com.deepu.orderprocessingservice.feign.PaymentServiceClient;
import com.deepu.orderprocessingservice.feign.UserServiceClient;
import com.deepu.orderprocessingservice.repository.OrderRepository;
import com.deepu.orderprocessingservice.request.EmailDetailRequest;
import com.deepu.orderprocessingservice.request.OrderStatusRequest;
import com.deepu.orderprocessingservice.request.PaymentInitiationRequest;
import com.deepu.orderprocessingservice.response.CartObject;
import com.deepu.orderprocessingservice.response.CommonResponse;
import com.deepu.orderprocessingservice.response.ProductObject;
import com.deepu.orderprocessingservice.response.UserDetailObject;
import com.deepu.orderprocessingservice.service.OrderService;
import com.deepu.orderprocessingservice.util.Mapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.rmi.NoSuchObjectException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartServiceClient cartServiceClient;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserServiceClient userServiceClient;
    @Autowired
    private PaymentServiceClient paymentServiceClient;
    @Autowired
    private NotificationServiceClient notificationServiceClient;

    @Override
    public CommonResponse orderProduct(String cartUniqueId,String token) throws Exception {
        final double DELIVERY_CHARGE = 5.00;
        final double HANDLING_FEE = 10.50;
        CartObject cartObject = getCartObjectFromClient(cartUniqueId);
        ProductObject productObject = cartObject.getProductObject();
        UserDetailObject userDetailObject = getUserDetailObjectFromClient();
        Order order = new Order();
        String orderId = generateOrderId(productObject.getUniqueId());
        order.setUniqueId(UUID.randomUUID().toString());
        order.setProductId(productObject.getUniqueId());
        order.setCartId(cartUniqueId);
        order.setOrderId(orderId);
        order.setEmail(cartObject.getEmail());
        order.setOrderDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        order.setProductPrice(productObject.getPrice());
        order.setDeliveryCharge(DELIVERY_CHARGE);
        order.setHandlingFee(HANDLING_FEE);
        order.setQuantity(cartObject.getQuantity());
        order.setAddressID(userDetailObject.getDeliveryAddress());
        PaymentInitiationRequest paymentInitiationRequest = new PaymentInitiationRequest();
        double totalAmount = DELIVERY_CHARGE + HANDLING_FEE + (productObject.getPrice() * cartObject.getQuantity());
        paymentInitiationRequest.setOrderId(orderId);
        paymentInitiationRequest.setAmount(totalAmount);
        paymentInitiationRequest.setJwt(token.substring(7));
        if(!isPaymentInitiated(paymentInitiationRequest)){
            throw new Exception("Payment Not Initialized");
        }
        order.setStatus(OrderStatus.ORDER_INITIATED.name());
        orderRepository.save(order);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(201);
        commonResponse.setStatus(ResponseStatus.CREATED);
        commonResponse.setData(Mapper.convertToOrderObject(order));
        commonResponse.setSuccessMessage(Constant.ORDER_CREATED_SUCCESS);
        return commonResponse;

    }

    @Override
    public CommonResponse getOrder(String orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(200);
        commonResponse.setStatus(ResponseStatus.SUCCESS);
        commonResponse.setData(Mapper.convertToOrderObject(order));
        commonResponse.setSuccessMessage(Constant.FETCH_ORDER_SUCCESS);
        return commonResponse;
    }

    @Override
    public CommonResponse confirmOrder(String orderId,OrderStatusRequest orderStatusRequest) {
        Order order = orderRepository.findByOrderId(orderId);
        cartServiceClient.deleteCartByUniqueId(order.getCartId());
        order.setDeliverDate(orderStatusRequest.getDeliverDate());
        order.setStatus(orderStatusRequest.getStatus());
        order.setCartId(null);
        orderRepository.save(order);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(200);
        commonResponse.setStatus(ResponseStatus.SUCCESS);
        commonResponse.setData(Mapper.convertToOrderObject(order));
        commonResponse.setSuccessMessage(Constant.FETCH_CONFIRM_SUCCESS);
        return commonResponse;
    }

    private boolean isPaymentInitiated(PaymentInitiationRequest paymentInitiationRequest) {
        CommonResponse response =  paymentServiceClient.initiatePayment(paymentInitiationRequest).getBody();
        if(response!=null){
            if(response.getCode()==200){
                sendPaymentLinkThroughMail((String)response.getData());
                return true;
            }
        }
        return false;
    }

    private void sendPaymentLinkThroughMail(String paymentLink) {
        String subject = "Complete Your Payment to Confirm Your Order";
        String body = String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);">
                    <h2 style="color: #333333; text-align: center;">Deepu's E-Commerce</h2>
                    <p style="color: #333333; font-size: 16px;">Dear Customer,</p>
                    <p style="color: #333333; font-size: 16px;">Thank you for your order! To confirm your order, please complete the payment using the link below:</p>
                    <div style="text-align: center; margin: 20px 0;">
                        <a href="%s" style="display: inline-block; background-color: #4CAF50; color: #ffffff; padding: 10px 20px; font-size: 18px; border-radius: 5px; text-decoration: none;">Complete Payment</a>
                    </div>
                    <p style="color: #333333; font-size: 16px;">%s <br> If you have any questions or need assistance, please contact our support team.</p>
                    <p style="color: #333333; font-size: 16px;">Thank you for choosing Deepu's E-Commerce!</p>
                    <p style="color: #333333; font-size: 16px;">Best regards,<br/>Deepu's E-Commerce Team</p>
                </div>
            </body>
            </html>
            """, paymentLink,paymentLink);
        EmailDetailRequest emailDetailRequest = new EmailDetailRequest();
        emailDetailRequest.setRecipient(getEmailFromSecurityContextHolder());
        emailDetailRequest.setSubject(subject);
        emailDetailRequest.setMsgBody(body);
        notificationServiceClient.sendEmail(emailDetailRequest);
    }

    private String generateOrderId(String uniqueId) {
        StringBuilder orderId = new StringBuilder("OD");
        Random random = new Random();
        int num = 1000 + random.nextInt(9999);
        orderId.append(num);
        orderId.append(uniqueId.substring(24));
        return orderId.toString();
    }

    private UserDetailObject getUserDetailObjectFromClient() throws JsonProcessingException, NoSuchObjectException {
        CommonResponse commonResponse = userServiceClient.getUserDetails().getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<UserDetailObject> typeReference = new TypeReference<>() {};
        UserDetailObject userDetailObject = objectMapper.readValue(
                objectMapper.writeValueAsString(
                        commonResponse != null ? commonResponse.getData() :null
                ),typeReference
        );
        if(userDetailObject==null){
            throw new NoSuchObjectException(Constant.NO_USER_DETAILS_AVAILABLE);
        }
        return userDetailObject;
    }

    private CartObject getCartObjectFromClient(String cartUniqueId) throws JsonProcessingException, NoSuchObjectException {
        CommonResponse commonResponse = cartServiceClient.getProductFromCart(cartUniqueId).getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<CartObject> typeReference = new TypeReference<>() {};
        CartObject cartObject = objectMapper.readValue(
                objectMapper.writeValueAsString(
                        commonResponse != null ? commonResponse.getData() :null
                ),typeReference
        );
        if(cartObject==null){
            throw new NoSuchObjectException(Constant.NO_CART_AVAILABLE);
        }
        return cartObject;
    }
    public String getEmailFromSecurityContextHolder(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
