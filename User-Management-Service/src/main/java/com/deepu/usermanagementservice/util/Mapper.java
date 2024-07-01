package com.deepu.usermanagementservice.util;

import com.deepu.usermanagementservice.entity.Address;
import com.deepu.usermanagementservice.entity.User;
import com.deepu.usermanagementservice.entity.UserDetail;
import com.deepu.usermanagementservice.request.AddressObject;
import com.deepu.usermanagementservice.request.UserObject;
import com.deepu.usermanagementservice.response.UserDetailObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public static UserDetailObject convertToUserDetailObject(UserDetail userDetail) {
        UserDetailObject userDetailObject = new UserDetailObject();
        userDetailObject.setPhoneNumber(userDetail.getPhoneNumber());
        userDetailObject.setLastName(userDetail.getLastName());
        userDetailObject.setGender(userDetail.getGender());
        userDetailObject.setFirstName(userDetail.getFirstName());
        userDetailObject.setEmail(userDetail.getEmail());
        userDetailObject.setDob(userDetail.getDob());
        userDetailObject.setAge(userDetail.getAge());
        userDetailObject.setDeliveryAddress(userDetail.getDeliveryAddress());
        List<UUID> addressIds = new ArrayList<>();
        for(Address address : userDetail.getAddresses()){
            addressIds.add(address.getId());
        }
        userDetailObject.setAddressIds(addressIds);
        return userDetailObject;
    }

    public static AddressObject convertToAddressObject(Address address) {
        AddressObject addressObject = new AddressObject();
        addressObject.setAddressType(address.getAddressType());
        addressObject.setArea(address.getArea());
        addressObject.setCity(address.getCity());
        addressObject.setHouseNo(address.getHouseNo());
        addressObject.setId(address.getId());
        addressObject.setPinCode(address.getPinCode());
        addressObject.setState(address.getState());
        return addressObject;
    }
}
