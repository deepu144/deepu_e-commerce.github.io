package com.deepu.paymentservice.feign;

import com.deepu.paymentservice.config.FeignConfig;
import com.deepu.paymentservice.request.OrderStatusRequest;
import com.deepu.paymentservice.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "ORDER-PROCESSING-SERVICE",path = "/order",configuration = FeignConfig.class)
public interface OrderServiceClient {
    @PutMapping("/{order-id}")
    ResponseEntity<CommonResponse> confirmOrder(@PathVariable("order-id") String orderId,@RequestBody OrderStatusRequest orderStatusRequest);
    @GetMapping("/{order-id}")
    ResponseEntity<CommonResponse> getOrderByOrderId(@PathVariable("order-id") String orderId);
}
