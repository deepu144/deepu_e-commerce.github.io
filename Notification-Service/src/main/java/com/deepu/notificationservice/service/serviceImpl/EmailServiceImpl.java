package com.deepu.notificationservice.service.serviceImpl;

import com.deepu.notificationservice.constant.Constant;
import com.deepu.notificationservice.enumeration.ResponseStatus;
import com.deepu.notificationservice.request.EmailDetailRequest;
import com.deepu.notificationservice.response.CommonResponse;
import com.deepu.notificationservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;


    @Override
    public CommonResponse sendSimpleEmail(EmailDetailRequest emailDetailRequest) {
        if(emailDetailRequest.getRecipient()==null){
            throw new MailSendException("Email is not Provided");
        }
        try {
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailDetailRequest.getRecipient());
            mailMessage.setText(emailDetailRequest.getMsgBody());
            mailMessage.setSubject(emailDetailRequest.getSubject());
            javaMailSender.send(mailMessage);
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setCode(200);
            commonResponse.setData(null);
            commonResponse.setStatus(ResponseStatus.SUCCESS);
            commonResponse.setSuccessMessage(Constant.MAIL_SENT_SUCCESS);
            return commonResponse;
        } catch (Exception e) {
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setCode(400);
            commonResponse.setData(null);
            commonResponse.setStatus(ResponseStatus.FAILED);
            commonResponse.setSuccessMessage(Constant.ERROR_MAIL);
            return commonResponse;
        }
    }
}
