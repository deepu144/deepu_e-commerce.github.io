package com.deepu.usermanagementservice.controller.controllerImpl;

import com.deepu.usermanagementservice.constant.Constant;
import com.deepu.usermanagementservice.controller.UserController;
import com.deepu.usermanagementservice.enumeration.ResponseStatus;
import com.deepu.usermanagementservice.request.AddressObject;
import com.deepu.usermanagementservice.request.UserDetailRequest;
import com.deepu.usermanagementservice.response.CommonResponse;
import com.deepu.usermanagementservice.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserControllerImpl implements UserController {

    public static final Logger log = LoggerFactory.getLogger(UserControllerImpl.class);
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Override
    @GetMapping("/")
    public ResponseEntity<CommonResponse> getUserDetails(){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserDetailsByEmail());
        }catch(Exception e){
            log.error("** getUserDetails: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Override
    @PutMapping("/")
    public ResponseEntity<CommonResponse> updateUserDetails(@RequestBody @Valid UserDetailRequest userDetailRequest , BindingResult result){
        if(result.hasErrors()){
            log.error("** updateUserDetails: {}", Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setCode(400);
            commonResponse.setStatus(ResponseStatus.FAILED);
            commonResponse.setData(null);
            commonResponse.setErrorMessage(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
        }
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserDetails(userDetailRequest));
        }catch(Exception e){
            log.error("** updateUserDetails: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Override
    @PostMapping("/address/add")
    public ResponseEntity<CommonResponse> addAddressToUserDetail(@RequestBody @Valid AddressObject addressObject , BindingResult result){
        if(result.hasErrors()){
            log.error("** addAddressToUserDetail: {}", Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setCode(400);
            commonResponse.setStatus(ResponseStatus.FAILED);
            commonResponse.setData(null);
            commonResponse.setErrorMessage(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
        }
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.addAddressToUserDetails(addressObject));
        }catch(Exception e){
            log.error("** addAddressToUserDetail: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Override
    @GetMapping("/address")
    public ResponseEntity<CommonResponse> getAddressById(@RequestParam UUID addressUniqueId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.getAddressById(addressUniqueId));
        }catch(Exception e){
            log.error("** getAddressById: {}",e.getMessage());
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
