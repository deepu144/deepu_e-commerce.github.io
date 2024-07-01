package com.deepu.orderprocessingservice.controller.contollerImpl;

import com.deepu.orderprocessingservice.constant.Constant;
import com.deepu.orderprocessingservice.controller.OrderController;
import com.deepu.orderprocessingservice.enumeration.ResponseStatus;
import com.deepu.orderprocessingservice.request.OrderStatusRequest;
import com.deepu.orderprocessingservice.response.CommonResponse;
import com.deepu.orderprocessingservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderControllerImpl implements OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderControllerImpl.class);
    @Autowired
    private OrderService orderService;

    @Override
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PostMapping("/")
    public ResponseEntity<CommonResponse> orderProduct(@RequestParam String cartUniqueId,@RequestHeader(value = "Authorization",required = false) String token){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(orderService.orderProduct(cartUniqueId,token));
        }catch (Exception e){
            log.error("** getTotalPriceFromUserCart: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Override
    @GetMapping("/{order-id}")
    public ResponseEntity<CommonResponse> getOrderByOrderId(@PathVariable("order-id") String orderId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrder(orderId));
        }catch (Exception e){
            log.error("** geOrderByOrderId: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Override
    @PutMapping("/{order-id}")
    public ResponseEntity<CommonResponse> confirmOrder(@PathVariable("order-id") String orderId,@RequestBody OrderStatusRequest orderStatusRequest){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(orderService.confirmOrder(orderId,orderStatusRequest));
        }catch (Exception e){
            log.error("** confirmOrder: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    public CommonResponse setServerError(Exception e){
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(500);
        commonResponse.setStatus(ResponseStatus.FAILED);
        commonResponse.setData(e.getMessage());
        commonResponse.setErrorMessage(Constant.SERVER_ERROR_MESSAGE);
        return commonResponse;
    }

}
