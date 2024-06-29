package com.deepu.usermanagementservice.service.serviceImpl;

import com.deepu.usermanagementservice.constant.Constant;
import com.deepu.usermanagementservice.entity.OtpInfo;
import com.deepu.usermanagementservice.entity.User;
import com.deepu.usermanagementservice.entity.UserDetail;
import com.deepu.usermanagementservice.enumeration.ResponseStatus;
import com.deepu.usermanagementservice.repository.AddressRepository;
import com.deepu.usermanagementservice.repository.OtpInfoRepository;
import com.deepu.usermanagementservice.repository.UserDetailRepository;
import com.deepu.usermanagementservice.repository.UserRepository;
import com.deepu.usermanagementservice.request.AuthenticationRequest;
import com.deepu.usermanagementservice.request.UserObject;
import com.deepu.usermanagementservice.request.VerifyUserRequest;
import com.deepu.usermanagementservice.response.CommonResponse;
import com.deepu.usermanagementservice.service.UserService;
import com.deepu.usermanagementservice.util.JWTUtils;
import com.deepu.usermanagementservice.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

    private static final Long EXPIRE_TIME = 300000L;

    @Override
    public CommonResponse signUpUser(UserObject userObject) throws AuthenticationException {
        if(!userObject.getPassword().equals(userObject.getConfirmPassword())){
            throw new AuthenticationException("Password Not Matching");
        }
        long currentTimeMillis = System.currentTimeMillis();
        long otpValidTimeMillis = currentTimeMillis + EXPIRE_TIME;
        String generatedOtp = this.generateOtp();
        OtpInfo otpInfo = new OtpInfo();
        otpInfo.setEmail(userObject.getEmail());
        otpInfo.setPassword(passwordEncoder.encode(userObject.getPassword()));
        otpInfo.setRole(userObject.getRole());
        otpInfo.setOtp(generatedOtp);
        otpInfo.setCreatedAt(System.currentTimeMillis());
        otpInfo.setExpireAt(otpValidTimeMillis);
        otpInfoRepository.save(otpInfo);

//        SEND OTP VIS EMAIL HERE

        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(200);
        commonResponse.setData(userObject);
        commonResponse.setStatus(ResponseStatus.SUCCESS);
        commonResponse.setSuccessMessage(Constant.OTP_SENT_SUCCESS);
        return commonResponse;
    }

    @Override
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

//          USER ACCOUNT VERIFIED AND SENT AN SUCCESS EMAIL

            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setCode(201);
            commonResponse.setData(userObject);
            commonResponse.setStatus(ResponseStatus.CREATED);
            commonResponse.setSuccessMessage(Constant.USER_VERIFIED);
            return commonResponse;
        }
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
}
