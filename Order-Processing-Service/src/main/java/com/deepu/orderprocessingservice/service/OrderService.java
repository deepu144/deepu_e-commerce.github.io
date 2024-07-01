package com.deepu.orderprocessingservice.service;

import com.deepu.orderprocessingservice.request.OrderStatusRequest;
import com.deepu.orderprocessingservice.response.CommonResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.rmi.NoSuchObjectException;

public interface OrderService {
    CommonResponse orderProduct(String cartUniqueId,String token) throws Exception;

    CommonResponse getOrder(String orderId);

    CommonResponse confirmOrder(String orderId,OrderStatusRequest orderStatusRequest);
}
