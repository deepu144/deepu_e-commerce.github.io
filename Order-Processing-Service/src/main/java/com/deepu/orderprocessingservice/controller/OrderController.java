package com.deepu.orderprocessingservice.controller;

import com.deepu.orderprocessingservice.request.OrderStatusRequest;
import com.deepu.orderprocessingservice.response.CommonResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface OrderController {

    @ApiResponse(description = "Order Product in Cart By providing Card-Unique-Id")
    @PostMapping("/")
    ResponseEntity<CommonResponse> orderProduct(@RequestParam String cartUniqueId, @RequestHeader(value = "Authorization",required = false) String token);

    @ApiResponse(description = "Get order by Order-Id")
    @GetMapping("/{order-id}")
    ResponseEntity<CommonResponse> getOrderByOrderId(@PathVariable("order-id") String orderId);

    @ApiResponse(description = "Update Confirmation order and Update Order")
    @PutMapping("/{order-id}")
    ResponseEntity<CommonResponse> confirmOrder(@PathVariable("order-id") String orderId,@RequestBody OrderStatusRequest orderStatusRequest);

}
