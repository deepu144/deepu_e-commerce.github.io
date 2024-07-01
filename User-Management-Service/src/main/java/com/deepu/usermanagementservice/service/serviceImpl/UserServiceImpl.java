package com.deepu.usermanagementservice.service.serviceImpl;

import com.deepu.usermanagementservice.constant.Constant;
import com.deepu.usermanagementservice.entity.Address;
import com.deepu.usermanagementservice.entity.OtpInfo;
import com.deepu.usermanagementservice.entity.User;
import com.deepu.usermanagementservice.entity.UserDetail;
import com.deepu.usermanagementservice.enumeration.ResponseStatus;
import com.deepu.usermanagementservice.feign.NotificationServiceClient;
import com.deepu.usermanagementservice.repository.AddressRepository;
import com.deepu.usermanagementservice.repository.OtpInfoRepository;
import com.deepu.usermanagementservice.repository.UserDetailRepository;
import com.deepu.usermanagementservice.repository.UserRepository;
import com.deepu.usermanagementservice.request.*;
import com.deepu.usermanagementservice.response.CommonResponse;
import com.deepu.usermanagementservice.request.EmailDetailRequest;
import com.deepu.usermanagementservice.response.UserDetailObject;
import com.deepu.usermanagementservice.service.UserService;
import com.deepu.usermanagementservice.util.JWTUtils;
import com.deepu.usermanagementservice.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.naming.AuthenticationException;
import java.rmi.NoSuchObjectException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private OtpInfoRepository otpInfoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserDetailRepository userDetailRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private NotificationServiceClient notificationServiceClient;
    private static final Long EXPIRE_TIME = 300000L;

    @Override
    public CommonResponse signUpUser(UserObject userObject) throws AuthenticationException {
        if(!userObject.getPassword().equals(userObject.getConfirmPassword())){
            throw new AuthenticationException("Password Not Matching");
        }
        if(userRepository.existsByEmail(userObject.getEmail())){
            throw new AuthenticationException(Constant.USER_ALREADY_EXISTS);
        }
        OtpInfo otpInfo;
        if(otpInfoRepository.existsById(userObject.getEmail())){
            otpInfo = otpInfoRepository.findById(userObject.getEmail()).get();
        }else{
            otpInfo = new OtpInfo();
            otpInfo.setEmail(userObject.getEmail());
            otpInfo.setPassword(passwordEncoder.encode(userObject.getPassword()));
            otpInfo.setRole(userObject.getRole());
        }
        long currentTimeMillis = System.currentTimeMillis();
        long otpValidTimeMillis = currentTimeMillis + EXPIRE_TIME;
        String generatedOtp = this.generateOtp();
        otpInfo.setOtp(generatedOtp);
        otpInfo.setCreatedAt(System.currentTimeMillis());
        otpInfo.setExpireAt(otpValidTimeMillis);
        otpInfoRepository.save(otpInfo);
        sendOTPMail(generatedOtp,userObject.getEmail());
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(200);
        commonResponse.setData(userObject);
        commonResponse.setStatus(ResponseStatus.SUCCESS);
        commonResponse.setSuccessMessage(Constant.OTP_SENT_SUCCESS);
        return commonResponse;
    }

    private void sendOTPMail(String generatedOtp,String email) {
        final String subject = "Your OTP for Deepu's E-Commerce";
        final String body = String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);">
                    <h2 style="color: #333333; text-align: center;">Deepu's E-Commerce</h2>
                    <p style="color: #333333; font-size: 16px;">Dear Customer,</p>
                    <p style="color: #333333; font-size: 16px;">Thank you for using Deepu's E-Commerce! Your One-Time Password (OTP) for verification is:</p>
                    <div style="text-align: center; margin: 20px 0;">
                        <span style="display: inline-block; background-color: #4CAF50; color: #ffffff; padding: 10px 20px; font-size: 18px; border-radius: 5px;">%s</span>
                    </div>
                    <p style="color: #333333; font-size: 16px;">Please use this OTP to complete your verification process. The OTP is valid for the next 5 minutes.</p>
                    <p style="color: #333333; font-size: 16px;">If you did not request this OTP, please ignore this email or contact our support team.</p>
                    <p style="color: #333333; font-size: 16px;">Best regards,<br/>Deepu's E-Commerce Team</p>
                </div>
            </body>
            </html>
            """, generatedOtp);
        EmailDetailRequest emailDetailRequest = new EmailDetailRequest();
        emailDetailRequest.setRecipient(email);
        emailDetailRequest.setSubject(subject);
        emailDetailRequest.setMsgBody(body);
        notificationServiceClient.sendEmail(emailDetailRequest);
    }

    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return Integer.toString(otp);
    }

    @Override
    public CommonResponse verifyUser(VerifyUserRequest verifyUserRequest) throws AuthenticationException {
        Optional<OtpInfo> otpInfoOptional = otpInfoRepository.findById(verifyUserRequest.getEmail());
        if(otpInfoOptional.isEmpty()){
            throw new AuthenticationException(Constant.SIGNUP_BEFORE);
        }
        OtpInfo otpInfo = otpInfoOptional.get();
        if(!otpInfo.getOtp().equals(verifyUserRequest.getOtp())){
            throw new AuthenticationException(Constant.INVALID_OTP);
        }else{
            long currentTimeMillis = System.currentTimeMillis();
            long expireTimeMillis = otpInfo.getExpireAt();
            if(currentTimeMillis>expireTimeMillis){
                otpInfoRepository.delete(otpInfo);
                throw new AuthenticationException(Constant.OTP_EXPIRED);
            }
            User user = new User();
            user.setEmail(otpInfo.getEmail());
            user.setPassword(otpInfo.getPassword());
            user.setRole(otpInfo.getRole());
            userRepository.save(user);
            UserDetail userDetail = new UserDetail();
            userDetail.setEmail(otpInfo.getEmail());
            userDetailRepository.save(userDetail);
            otpInfoRepository.delete(otpInfo);
            UserObject userObject = Mapper.convertToUserObject(user);
            this.accountVerifiedMail(user.getEmail());
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setCode(201);
            commonResponse.setData(userObject);
            commonResponse.setStatus(ResponseStatus.CREATED);
            commonResponse.setSuccessMessage(Constant.USER_VERIFIED);
            return commonResponse;
        }
    }

    public void accountVerifiedMail(String email){
        final String subject = "Your Account is Successfully Created and Verified!";
        final String body = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);">
                    <h2 style="color: #333333; text-align: center;">Deepu's E-Commerce</h2>
                    <p style="color: #333333; font-size: 16px;">Dear Customer,</p>
                    <p style="color: #333333; font-size: 16px;">We are excited to inform you that your account has been successfully created and verified.</p>
                    <p style="color: #333333; font-size: 16px;">You can now sign in and start enjoying our services.</p>
                    <div style="text-align: center; margin: 20px 0;">
                        <a href="http://localhost:8080/auth/signin" style="display: inline-block; background-color: #4CAF50; color: #ffffff; padding: 10px 20px; font-size: 18px; border-radius: 5px; text-decoration: none;">Sign In</a>
                    </div>
                    <p style="color: #333333; font-size: 16px;">Thank you for choosing Deepu's E-Commerce!</p>
                    <p style="color: #333333; font-size: 16px;">Best regards,<br/>Deepu's E-Commerce Team</p>
                </div>
            </body>
            </html>
            """;
        EmailDetailRequest emailDetailRequest = new EmailDetailRequest();
        emailDetailRequest.setRecipient(email);
        emailDetailRequest.setSubject(subject);
        emailDetailRequest.setMsgBody(body);
        notificationServiceClient.sendEmail(emailDetailRequest);
    }

    @Override
    public CommonResponse signInUser(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        HashMap<String,Object> claims = new HashMap<>();
        claims.put("roles",roles);
        String token = jwtUtils.generateToken(claims,userDetails);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(200);
        commonResponse.setStatus(ResponseStatus.SUCCESS);
        commonResponse.setData(token);
        commonResponse.setSuccessMessage(Constant.SIGN_IN_SUCCESS);
        return commonResponse;
    }

    @Override
    public CommonResponse getUserDetailsByEmail() throws NoSuchObjectException {
        String email = getEmailFromSecurityContextHolder();
        Optional<UserDetail> userDetailOptional = userDetailRepository.findById(email);
        if(userDetailOptional.isEmpty()){
            throw new NoSuchObjectException(Constant.USER_DETAIL_NOT_EXISTS);
        }
        UserDetail userDetail = userDetailOptional.get();
        UserDetailObject userDetailObject = Mapper.convertToUserDetailObject(userDetail);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(200);
        commonResponse.setStatus(ResponseStatus.SUCCESS);
        commonResponse.setData(userDetailObject);
        commonResponse.setSuccessMessage(Constant.FETCH_USER_DETAIL_SUCCESS);
        return commonResponse;
    }

    @Override
    public CommonResponse updateUserDetails(UserDetailRequest userDetailRequest) throws NoSuchObjectException {
        String email = getEmailFromSecurityContextHolder();
        Optional<UserDetail> userDetailOptional = userDetailRepository.findById(email);
        if(userDetailOptional.isEmpty()){
            throw new NoSuchObjectException(Constant.USER_DETAIL_NOT_EXISTS);
        }
        UserDetail userDetail = userDetailOptional.get();
        userDetail.setPhoneNumber(userDetailRequest.getPhoneNumber());
        userDetail.setLastName(userDetailRequest.getLastName());
        userDetail.setGender(userDetailRequest.getGender());
        userDetail.setFirstName(userDetailRequest.getFirstName());
        userDetail.setDob(userDetailRequest.getDob());
        userDetail.setAge(userDetailRequest.getAge());
        if(userDetailRequest.getDeliveryAddress()!=null){
            userDetail.setDeliveryAddress(userDetailRequest.getDeliveryAddress());
        }
        userDetailRepository.save(userDetail);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(200);
        commonResponse.setStatus(ResponseStatus.SUCCESS);
        commonResponse.setData(Mapper.convertToUserDetailObject(userDetail));
        commonResponse.setSuccessMessage(Constant.UPDATE_USER_DETAIL_SUCCESS);
        return commonResponse;
    }

    @Override
    public CommonResponse addAddressToUserDetails(AddressObject addressObject) throws NoSuchObjectException {
        String email = getEmailFromSecurityContextHolder();
        Optional<UserDetail> userDetailOptional = userDetailRepository.findById(email);
        if(userDetailOptional.isEmpty()){
            throw new NoSuchObjectException(Constant.USER_DETAIL_NOT_EXISTS);
        }
        UserDetail userDetail = userDetailOptional.get();
        Address address = new Address();
        address.setAddressType(addressObject.getAddressType());
        address.setArea(addressObject.getArea());
        address.setCity(addressObject.getCity());
        address.setHouseNo(addressObject.getHouseNo());
        address.setPinCode(addressObject.getPinCode());
        address.setState(addressObject.getState());
        address.setUserDetail(userDetail);
        addressRepository.save(address);
        userDetail.getAddresses().add(address);
        userDetailRepository.save(userDetail);
        addressObject.setId(address.getId());
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(201);
        commonResponse.setStatus(ResponseStatus.CREATED);
        commonResponse.setData(addressObject);
        commonResponse.setSuccessMessage(Constant.ADDRESS_ADDED_SUCCESS);
        return commonResponse;
    }

    @Override
    public CommonResponse getAddressById(UUID addressUniqueId) throws NoSuchObjectException {
        Optional<Address> addressOptional = addressRepository.findById(addressUniqueId);
        if(addressOptional.isEmpty()){
            throw new NoSuchObjectException(Constant.ADDRESS_NOT_EXISTS);
        }
        Address address = addressOptional.get();
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(200);
        commonResponse.setStatus(ResponseStatus.SUCCESS);
        commonResponse.setData(Mapper.convertToAddressObject(address));
        commonResponse.setSuccessMessage(Constant.ADDRESS_FETCH_SUCCESS);
        return commonResponse;
    }

    public String getEmailFromSecurityContextHolder(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

}
