package com.deepu.notificationservice.controller.controllerImpl;

import com.deepu.notificationservice.constant.Constant;
import com.deepu.notificationservice.controller.EmailController;
import com.deepu.notificationservice.enumeration.ResponseStatus;
import com.deepu.notificationservice.request.EmailDetailRequest;
import com.deepu.notificationservice.response.CommonResponse;
import com.deepu.notificationservice.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class EmailControllerImpl implements EmailController {

    private final Logger log = LoggerFactory.getLogger(EmailControllerImpl.class);
    @Autowired
    private EmailService emailService;

    @Override
    @PostMapping("/send")
    public ResponseEntity<CommonResponse> sendEmail(@RequestBody EmailDetailRequest emailDetailRequest) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(emailService.sendSimpleEmail(emailDetailRequest));
        }catch (Exception e){
            log.error("** sendEmail: {}",e.getMessage());
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
