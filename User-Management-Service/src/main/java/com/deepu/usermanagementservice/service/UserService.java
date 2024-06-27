package com.deepu.usermanagementservice.service;

import com.deepu.usermanagementservice.request.AuthenticationRequest;
import com.deepu.usermanagementservice.request.UserObject;
import com.deepu.usermanagementservice.request.VerifyUserRequest;
import com.deepu.usermanagementservice.response.CommonResponse;
import org.springframework.web.bind.annotation.RequestBody;

import javax.naming.AuthenticationException;

public interface UserService {
    CommonResponse signUpUser(UserObject userObject) throws AuthenticationException;
    String generateOtp();
    CommonResponse verifyUser(VerifyUserRequest verifyUserRequest) throws AuthenticationException;
    CommonResponse signInUser(@RequestBody AuthenticationRequest authenticationRequest);
}
