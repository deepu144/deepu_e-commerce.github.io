package com.deepu.paymentservice.controller.controllerImpl;

import com.deepu.paymentservice.constant.Constant;
import com.deepu.paymentservice.controller.PaymentController;
import com.deepu.paymentservice.enumeration.ResponseStatus;
import com.deepu.paymentservice.request.PaymentInitiationRequest;
import com.deepu.paymentservice.request.StripeChargeRequest;
import com.deepu.paymentservice.response.CommonResponse;
import com.deepu.paymentservice.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
public class PaymentControllerImpl implements PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentControllerImpl.class);
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<CommonResponse> initiatePayment(@RequestBody PaymentInitiationRequest request) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(paymentService.initiatePayment(request));
        }catch (Exception e){
            log.error("** initiatePayment: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    @PostMapping("/charge")
    @ResponseBody
    public ResponseEntity<CommonResponse> charge(@RequestBody StripeChargeRequest request) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(paymentService.charge(request));
        }catch (Exception e){
            log.error("** charge: {}",e.getMessage());
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
