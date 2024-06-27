package com.deepu.usermanagementservice.controller;

import com.deepu.usermanagementservice.constant.Constant;
import com.deepu.usermanagementservice.request.AuthenticationRequest;
import com.deepu.usermanagementservice.request.UserObject;
import com.deepu.usermanagementservice.request.VerifyUserRequest;
import com.deepu.usermanagementservice.response.CommonResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthController {

    @ApiResponse(description = "Signing Up the new User")
    @PostMapping("/signup")
    ResponseEntity<CommonResponse> userSignUp(@RequestBody @Valid UserObject userObject , BindingResult result);

    @ApiResponse(description = "Verify the User Account By Otp ")
    @PostMapping("/verify")
    ResponseEntity<CommonResponse> verifyUser(@RequestBody @Valid VerifyUserRequest verifyUserRequest
            , BindingResult result);
    @ApiResponse(description = "User SignIn")
    @PostMapping("/signin")
    ResponseEntity<CommonResponse> userSignIn(@RequestBody @Valid AuthenticationRequest request , BindingResult result);

}
