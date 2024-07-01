package com.deepu.usermanagementservice.controller.controllerImpl;

import com.deepu.usermanagementservice.constant.Constant;
import com.deepu.usermanagementservice.controller.AuthController;
import com.deepu.usermanagementservice.enumeration.ResponseStatus;
import com.deepu.usermanagementservice.request.AuthenticationRequest;
import com.deepu.usermanagementservice.request.UserObject;
import com.deepu.usermanagementservice.request.VerifyUserRequest;
import com.deepu.usermanagementservice.response.CommonResponse;
import com.deepu.usermanagementservice.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthControllerImpl implements AuthController {

    public static final Logger log = LoggerFactory.getLogger(AuthControllerImpl.class);
    @Autowired
    private UserService userService;

    @Override
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> userSignUp(@RequestBody @Valid UserObject userObject , BindingResult result){
        if(result.hasErrors()){
            log.error("** userSignUp: {}", Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setCode(400);
            commonResponse.setStatus(ResponseStatus.FAILED);
            commonResponse.setData(null);
            commonResponse.setErrorMessage(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
        }
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.signUpUser(userObject));
        }catch(Exception e){
            log.error("** userSignUp: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    @Override
    @PostMapping("/verify")
    public ResponseEntity<CommonResponse> verifyUser(@RequestBody @Valid VerifyUserRequest verifyUserRequest ,
                                                     BindingResult result) {
        if(result.hasErrors()){
            log.error("** verifyUser: {}", Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setCode(400);
            commonResponse.setStatus(ResponseStatus.FAILED);
            commonResponse.setData(null);
            commonResponse.setErrorMessage(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
        }
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.verifyUser(verifyUserRequest));
        }catch(Exception e){
            log.error("** verifyUser: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<CommonResponse> userSignIn(@RequestBody @Valid AuthenticationRequest request , BindingResult result){
        if(result.hasErrors()){
            log.error("** userSignIn: {}", Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setCode(400);
            commonResponse.setStatus(ResponseStatus.FAILED);
            commonResponse.setData(null);
            commonResponse.setErrorMessage(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
        }
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.signInUser(request));
        }catch(Exception e){
            log.error("** userSignIn: {}",e.getMessage());
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
