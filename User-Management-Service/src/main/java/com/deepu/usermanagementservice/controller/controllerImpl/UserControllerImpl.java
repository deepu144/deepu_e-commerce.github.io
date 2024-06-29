package com.deepu.usermanagementservice.controller.controllerImpl;

import com.deepu.usermanagementservice.controller.UserController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserControllerImpl implements UserController {

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/all")
    String home(){
        return "Hello World";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    String admin(){
        return "ADMIN ONLY";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/user")
    String user(){
        return "USER ONLY";
    }
}
