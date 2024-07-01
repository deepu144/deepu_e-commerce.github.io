package com.deepu.shoppingcartservice.service.serviceImpl;

import com.deepu.shoppingcartservice.constant.Constant;
import com.deepu.shoppingcartservice.entity.Cart;
import com.deepu.shoppingcartservice.enumeration.ResponseStatus;
import com.deepu.shoppingcartservice.feign.ProductServiceClient;
import com.deepu.shoppingcartservice.repository.CartRepository;
import com.deepu.shoppingcartservice.request.CartRequest;
import com.deepu.shoppingcartservice.response.CartObject;
import com.deepu.shoppingcartservice.response.CommonResponse;
import com.deepu.shoppingcartservice.response.ListResponse;
import com.deepu.shoppingcartservice.response.ProductObject;
import com.deepu.shoppingcartservice.service.CartService;
import com.deepu.shoppingcartservice.util.Mapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductServiceClient productServiceClient;

    @Override
    public CommonResponse addProductToCart(String productUniqueId,long quantity) throws Exception {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        ProductObject productObject = getProductObject(productUniqueId);
        if (!productObject.getIsStockAvailable()){
            throw new Exception(Constant.STOCK_NOT_AVAILABLE);
        }
        if(productObject.getStock()< quantity){
            throw new Exception(Constant.NOT_ENOUGH_QUANTITY);
        }
        Cart cart = new Cart();
        cart.setUniqueId(UUID.randomUUID().toString());
        cart.setProductId(productUniqueId);
        cart.setQuantity(quantity);
        cart.setEmail(email);
        cartRepository.save(cart);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(201);
        commonResponse.setData(Mapper.convertToCartObject(cart,productObject));
        commonResponse.setStatus(ResponseStatus.CREATED);
        commonResponse.setSuccessMessage(Constant.CART_ADDED_SUCCESS);
        return commonResponse;
    }

    @Override
    public CommonResponse getProductFromCart(String cartUniqueId) throws Exception {
        Cart cart = cartRepository.findByUniqueId(cartUniqueId);
        if(cart==null){
            throw new NoSuchObjectException(Constant.NO_CART_AVAILABLE);
        }
        ProductObject productObject = getProductObject(cart.getProductId());
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(200);
        commonResponse.setData(Mapper.convertToCartObject(cart,productObject));
        commonResponse.setStatus(ResponseStatus.SUCCESS);
        commonResponse.setSuccessMessage(Constant.CART_FETCH_SUCCESS);
        return commonResponse;
    }

    @Override
    public CommonResponse updateCart(String cartUniqueId, CartRequest cartRequest) throws Exception {
        Cart cart = cartRepository.findByUniqueId(cartUniqueId);
        if(cart==null){
            throw new NoSuchObjectException(Constant.NO_CART_AVAILABLE);
        }
        if(cartRequest.getQuantity()==0){
            return deleteCart(cartUniqueId);
        }
        ProductObject productObject = getProductObject(cart.getProductId());
        if (!productObject.getIsStockAvailable()){
            throw new Exception(Constant.STOCK_NOT_AVAILABLE);
        }
        if(productObject.getStock()< cartRequest.getQuantity()){
            throw new Exception(Constant.NOT_ENOUGH_QUANTITY);
        }
        cart.setProductId(cartRequest.getProductId());
        cart.setQuantity(cartRequest.getQuantity());
        cartRepository.save(cart);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(200);
        commonResponse.setData(Mapper.convertToCartObject(cart,productObject));
        commonResponse.setStatus(ResponseStatus.SUCCESS);
        commonResponse.setSuccessMessage(Constant.CART_UPDATE_SUCCESS);
        return commonResponse;
    }

    @Override
    public CommonResponse deleteCart(String cartUniqueId) throws NoSuchObjectException {
        Cart cart = cartRepository.findByUniqueId(cartUniqueId);
        if(cart==null){
            throw new NoSuchObjectException(Constant.NO_CART_AVAILABLE);
        }
        cartRepository.delete(cart);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(200);
        commonResponse.setData(null);
        commonResponse.setStatus(ResponseStatus.SUCCESS);
        commonResponse.setSuccessMessage(Constant.CART_DELETE_SUCCESS);
        return commonResponse;
    }

    @Override
    public CommonResponse getCartByEmail() throws Exception {
        String email = getEmailFromSecurityContextHolder();
        List<Cart> cartList = cartRepository.findByEmail(email);
        List<CartObject> cartObjects = new ArrayList<>();
        for(Cart cart : cartList){
            cartObjects.add(Mapper.convertToCartObject(cart,getProductObject(cart.getProductId())));
        }
        ListResponse listResponse = new ListResponse(cartObjects.size(),cartObjects);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(200);
        commonResponse.setData(listResponse);
        commonResponse.setStatus(ResponseStatus.SUCCESS);
        commonResponse.setSuccessMessage(Constant.FETCH_CART_EMAIL_SUCCESS);
        return commonResponse;
    }

    @Override
    public CommonResponse getTotalPriceFromUserCartProduct() throws Exception {
        String email = getEmailFromSecurityContextHolder();
        double price = 0.0;
        List<Cart> cartList = cartRepository.findByEmail(email);
        for(Cart cart : cartList){
            price += ( getProductObject(cart.getProductId()).getPrice() * cart.getQuantity() );
        }
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(200);
        commonResponse.setData(price);
        commonResponse.setStatus(ResponseStatus.SUCCESS);
        commonResponse.setSuccessMessage(Constant.FETCH_TOTAL_CART_PRICE_SUCCESS);
        return commonResponse;
    }

    private ProductObject getProductObject(String productUniqueId) throws Exception {
        CommonResponse response = productServiceClient.getProductByUniqueId(productUniqueId).getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<ProductObject> typeReference = new TypeReference<>() {};
        ProductObject productObject = objectMapper.readValue(
                objectMapper.writeValueAsString(
                        response !=null ? response.getData() : null
                ),typeReference
        );
        if(productObject==null){
            throw new NoSuchObjectException(Constant.PRODUCT_NOT_AVAILABLE);
        }
        return productObject;
    }

    public String getEmailFromSecurityContextHolder(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

}
