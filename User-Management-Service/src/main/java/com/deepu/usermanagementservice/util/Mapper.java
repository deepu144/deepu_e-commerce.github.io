package com.deepu.usermanagementservice.util;

import com.deepu.usermanagementservice.entity.User;
import com.deepu.usermanagementservice.request.UserObject;

public class Mapper {

    public static User convertToUser(UserObject userObject){
        User user = new User();
        user.setEmail(userObject.getEmail());
        user.setPassword(userObject.getPassword());
        user.setRole(userObject.getRole());
        return user;
    }


    public static UserObject convertToUserObject(User user) {
        UserObject userObject = new UserObject();
        userObject.setId(user.getId());
        userObject.setEmail(user.getEmail());
        userObject.setRole(user.getRole());
        userObject.setPassword(user.getPassword());
        return userObject;
    }
}
