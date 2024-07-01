package com.deepu.usermanagementservice.controller;

import com.deepu.usermanagementservice.request.AddressObject;
import com.deepu.usermanagementservice.request.UserDetailRequest;
import com.deepu.usermanagementservice.response.CommonResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

public interface UserController {

    @ApiResponse(description = "Get the Details of User")
    @GetMapping("/")
    ResponseEntity<CommonResponse> getUserDetails();

    @ApiResponse(description = "Update User Details")
    @PutMapping("/")
    ResponseEntity<CommonResponse> updateUserDetails(@RequestBody @Valid UserDetailRequest userDetailRequest , BindingResult result);

    @ApiResponse(description = "Add Address to User Detail")
    @PostMapping("/address/add")
    ResponseEntity<CommonResponse> addAddressToUserDetail(@RequestBody @Valid AddressObject addressObject , BindingResult result);

    @ApiResponse(description = "Fetch Address By Unique-Address-Id")
    @GetMapping("/address")
    ResponseEntity<CommonResponse> getAddressById(@RequestParam UUID addressUniqueId);

}
