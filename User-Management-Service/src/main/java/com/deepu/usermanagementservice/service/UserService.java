package com.deepu.usermanagementservice.service;

import com.deepu.usermanagementservice.request.*;
import com.deepu.usermanagementservice.response.CommonResponse;
import org.springframework.web.bind.annotation.RequestBody;
import javax.naming.AuthenticationException;
import java.rmi.NoSuchObjectException;
import java.util.UUID;

public interface UserService {
    CommonResponse signUpUser(UserObject userObject) throws AuthenticationException;
    CommonResponse verifyUser(VerifyUserRequest verifyUserRequest) throws AuthenticationException;
    CommonResponse signInUser(@RequestBody AuthenticationRequest authenticationRequest);
    CommonResponse getUserDetailsByEmail() throws NoSuchObjectException;
    CommonResponse updateUserDetails(UserDetailRequest userDetailRequest) throws NoSuchObjectException;

    CommonResponse addAddressToUserDetails(AddressObject addressObject) throws NoSuchObjectException;

    CommonResponse getAddressById(UUID addressUniqueId) throws NoSuchObjectException;
}
