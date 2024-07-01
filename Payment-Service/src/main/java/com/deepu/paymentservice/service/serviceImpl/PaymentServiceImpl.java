package com.deepu.paymentservice.service.serviceImpl;

import com.deepu.paymentservice.constant.Constant;
import com.deepu.paymentservice.entity.Payment;
import com.deepu.paymentservice.enumeration.OrderStatus;
import com.deepu.paymentservice.enumeration.ResponseStatus;
import com.deepu.paymentservice.feign.NotificationServiceClient;
import com.deepu.paymentservice.feign.OrderServiceClient;
import com.deepu.paymentservice.feign.ProductServiceClient;
import com.deepu.paymentservice.feign.UserServiceClient;
import com.deepu.paymentservice.repository.PaymentRepository;
import com.deepu.paymentservice.request.EmailDetailRequest;
import com.deepu.paymentservice.request.OrderStatusRequest;
import com.deepu.paymentservice.request.PaymentInitiationRequest;
import com.deepu.paymentservice.request.StripeChargeRequest;
import com.deepu.paymentservice.response.*;
import com.deepu.paymentservice.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    @Value("${api.stripe.key}")
    private String stripeApiKey;
    @Autowired
    private OrderServiceClient orderServiceClient;
    @PostConstruct
    public void init(){
        Stripe.apiKey = stripeApiKey;
    }
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private NotificationServiceClient notificationServiceClient;
    @Autowired
    private ProductServiceClient productServiceClient;
    @Autowired
    private UserServiceClient userServiceClient;

    public CommonResponse initiatePayment(PaymentInitiationRequest request) {
        String email = getEmailFromSecurityContextHolder();
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setEmail(email);
        payment.setAmount(request.getAmount());
        payment.setStatus("INITIATED");
        payment.setDateTime(LocalDateTime.now());
        paymentRepository.save(payment);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(200);
        commonResponse.setStatus(ResponseStatus.SUCCESS);
        commonResponse.setData("localhost:8083/index.html?paymentId=" + payment.getId()+"&token="+request.getJwt());
        commonResponse.setSuccessMessage(Constant.PAYMENT_INITIATE_SUCCESS);
        return commonResponse;
    }

    public CommonResponse charge(StripeChargeRequest stripeChargeRequest) {
        try {
            Payment payment = paymentRepository.findById(stripeChargeRequest.getPaymentId())
                    .orElseThrow(() -> new RuntimeException("Invalid payment ID"));
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", (int) (payment.getAmount() * 100));
            chargeParams.put("currency", "USD");
            chargeParams.put("description", "Payment for order " + payment.getOrderId());
            chargeParams.put("source", stripeChargeRequest.getStripeToken());
            Charge charge = Charge.create(chargeParams);

            payment.setTransactionId(charge.getId());
            payment.setStatus(charge.getPaid() ? "COMPLETED" : "FAILED");
            payment.setDateTime(LocalDateTime.now());
            paymentRepository.save(payment);

            Date deliveryDate = Date.from(LocalDate.now().plusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant());
            this.confirmOrder(payment.getOrderId(),deliveryDate);
            this.getAndSendConfirmMail(payment.getOrderId(),payment.getTransactionId());

            StripeChargeObject stripeChargeObject = new StripeChargeObject();
            stripeChargeObject.setSuccess(charge.getPaid());
            stripeChargeObject.setMessage(charge.getOutcome().getSellerMessage());
            stripeChargeObject.setChargeId(charge.getId());
            CommonResponse response = new CommonResponse();
            response.setCode(200);
            response.setStatus(ResponseStatus.SUCCESS);
            response.setSuccessMessage(Constant.ORDER_CONFIRMED);
            response.setData(stripeChargeObject);
            return response;
        } catch (Exception e){
            log.error("** StripeService-charge : {}", e.getMessage());
            CommonResponse response = new CommonResponse();
            response.setCode(400);
            response.setStatus(ResponseStatus.FAILED);
            response.setSuccessMessage(Constant.PAYMENT_FAILED);
            response.setData(null);
            return response;
        }
    }

    private void getAndSendConfirmMail(String orderId, String transactionId) throws NoSuchObjectException, JsonProcessingException {
        OrderObject orderObject = getOrderObjectFromClient(orderId);
        ProductObject productObject = getProductObjectFromClient(orderObject.getProductId());
        UserDetailObject userDetailObject = getUserDetailObjectFromClient();
        AddressObject addressObject = getAddressObjectFromClient(userDetailObject.getDeliveryAddress());
        sendOrderConfirmMail(orderObject,productObject,userDetailObject,transactionId,addressObject);
    }

    private void sendOrderConfirmMail(OrderObject orderObject, ProductObject productObject, UserDetailObject userDetailObject,String transactionId, AddressObject addressObject) {
        String subject =  "Order Confirmation - Thank You for Your Purchase!";
        String body = String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);">
                    <h2 style="color: #4CAF50; text-align: center;">Deepu's E-Commerce</h2>
                    <p style="color: #333333; font-size: 16px;">Dear %s %s,</p>
                    <p style="color: #333333; font-size: 16px;">Thank you for your purchase! Your order has been confirmed. Here are the details:</p>
                    <table style="width: 100%%; margin: 20px 0; border-collapse: collapse;">
                        <tr>
                            <th style="text-align: left; padding: 8px; background-color: #f2f2f2;">Transaction ID</th>
                            <td style="padding: 8px;">%s</td>
                        </tr>
                        <tr>
                            <th style="text-align: left; padding: 8px; background-color: #f2f2f2;">Order ID</th>
                            <td style="padding: 8px;">%s</td>
                        </tr>
                        <tr>
                            <th style="text-align: left; padding: 8px; background-color: #f2f2f2;">Product Name</th>
                            <td style="padding: 8px;">%s</td>
                        </tr>
                        <tr>
                            <th style="text-align: left; padding: 8px; background-color: #f2f2f2;">Price</th>
                            <td style="padding: 8px;">$%.2f</td>
                        </tr>
                        <tr>
                            <th style="text-align: left; padding: 8px; background-color: #f2f2f2;">Quantity</th>
                            <td style="padding: 8px;">%d</td>
                        </tr>
                        <tr>
                            <th style="text-align: left; padding: 8px; background-color: #f2f2f2;">Total Amount</th>
                            <td style="padding: 8px;">$%.2f</td>
                        </tr>
                        <tr>
                            <th style="text-align: left; padding: 8px; background-color: #f2f2f2;">Order Date</th>
                            <td style="padding: 8px;">%tB %<te, %<tY</td>
                        </tr>
                        <tr>
                            <th style="text-align: left; padding: 8px; background-color: #f2f2f2;">Delivery Date</th>
                            <td style="padding: 8px;">%tB %<te, %<tY</td>
                        </tr>
                        <tr>
                            <th style="text-align: left; padding: 8px; background-color: #f2f2f2;">Delivery Address</th>
                            <td style="padding: 8px;">%s, %s, %s, %s - %d (%s)</td>
                        </tr>
                    </table>
                    <p style="color: #333333; font-size: 16px;">If you have any questions or need assistance, please contact our support team.</p>
                    <p style="color: #333333; font-size: 16px;">Thank you for choosing Deepu's E-Commerce!</p>
                    <p style="color: #333333; font-size: 16px;">Best regards,<br/>Deepu's E-Commerce Team</p>
                </div>
            </body>
            </html>
            """, userDetailObject.getFirstName(), userDetailObject.getLastName(),transactionId, orderObject.getOrderId(),
                productObject.getName(), orderObject.getProductPrice(), orderObject.getQuantity(),
                (orderObject.getProductPrice() + orderObject.getDeliveryCharge() + orderObject.getHandlingFee()),
                orderObject.getOrderDate(), orderObject.getDeliverDate(),
                addressObject.getHouseNo(), addressObject.getArea(), addressObject.getCity(),
                addressObject.getState(), addressObject.getPinCode(), addressObject.getAddressType());
        EmailDetailRequest emailDetailRequest = new EmailDetailRequest(getEmailFromSecurityContextHolder(),body,subject);
        notificationServiceClient.sendEmail(emailDetailRequest);
    }

    private AddressObject getAddressObjectFromClient(UUID deliveryAddress) throws JsonProcessingException, NoSuchObjectException {
        CommonResponse response = userServiceClient.getAddressById(deliveryAddress).getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<AddressObject> typeReference = new TypeReference<>() {};
        AddressObject addressObject = objectMapper.readValue(
                objectMapper.writeValueAsString(
                        response!=null ? response.getData() : null
                ),typeReference
        );
        if(addressObject==null){
            throw new NoSuchObjectException(Constant.ADDRESS_OBJECT_NOT_EXISTS);
        }
        return addressObject;
    }

    private UserDetailObject getUserDetailObjectFromClient() throws JsonProcessingException, NoSuchObjectException {
        CommonResponse response = userServiceClient.getUserDetails().getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<UserDetailObject> typeReference = new TypeReference<>() {};
        UserDetailObject userDetailObject = objectMapper.readValue(
                objectMapper.writeValueAsString(
                        response!=null ? response.getData() : null
                ),typeReference
        );
        if(userDetailObject==null){
            throw new NoSuchObjectException(Constant.USER_DETAIL_OBJECT_NOT_EXISTS);
        }
        return userDetailObject;
    }

    private ProductObject getProductObjectFromClient(String productId) throws JsonProcessingException, NoSuchObjectException {
        CommonResponse response = productServiceClient.getProductByUniqueId(productId).getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<ProductObject> typeReference = new TypeReference<>() {};
        ProductObject productObject = objectMapper.readValue(
                objectMapper.writeValueAsString(
                        response!=null ? response.getData() : null
                ),typeReference
        );
        if(productObject==null){
            throw new NoSuchObjectException(Constant.PRODUCT_OBJECT_NOT_EXISTS);
        }
        return productObject;
    }


    public OrderObject getOrderObjectFromClient(String orderId) throws JsonProcessingException, NoSuchObjectException {
        CommonResponse response = orderServiceClient.getOrderByOrderId(orderId).getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<OrderObject> typeReference = new TypeReference<>() {};
        OrderObject orderObject = objectMapper.readValue(
                objectMapper.writeValueAsString(
                        response!=null ? response.getData() : null
                ),typeReference
        );
        if(orderObject==null){
            throw new NoSuchObjectException(Constant.ORDER_OBJECT_NOT_EXISTS);
        }
        return orderObject;
    }

    public void confirmOrder(String orderId,Date deliveryDate) {
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest();
        orderStatusRequest.setStatus(OrderStatus.ORDER_CONFIRMED.name());
        orderStatusRequest.setDeliverDate(deliveryDate);
        orderServiceClient.confirmOrder(orderId,orderStatusRequest);
    }

    public String getEmailFromSecurityContextHolder(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

}

