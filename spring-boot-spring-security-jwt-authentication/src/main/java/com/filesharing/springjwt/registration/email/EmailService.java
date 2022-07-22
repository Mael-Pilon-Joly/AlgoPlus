package com.filesharing.springjwt.registration.email;

import com.wildbit.java.postmark.Postmark;
import com.wildbit.java.postmark.client.data.model.message.Message;
import com.wildbit.java.postmark.client.data.model.message.MessageResponse;
import com.wildbit.java.postmark.client.exception.PostmarkException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sendinblue.ApiClient;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);


    @Override
    @Async
    public void send(String dest, String email, String subject) {
        String API_KEY = "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";


        ApiClient defaultClient = Configuration.getDefaultApiClient();
        // Configure API key authorization: api-key
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey(API_KEY);

        try {
            TransactionalEmailsApi api = new TransactionalEmailsApi();
            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail("mael.pilon-joly@hotmail.com");
            sender.setName("AlgoPlus");
            List<SendSmtpEmailTo> toList = new ArrayList<SendSmtpEmailTo>();
            SendSmtpEmailTo to = new SendSmtpEmailTo();
            to.setEmail(dest);
            toList.add(to);
            SendSmtpEmailReplyTo replyTo = new SendSmtpEmailReplyTo();
            replyTo.setEmail("mael.pilon-joly@hotmail.com");
            replyTo.setName("Mael Pilon-Joly");
            Properties headers = new Properties();
            headers.setProperty("Some-Custom-Name", "unique-id-1234");
            Properties params = new Properties();
            params.setProperty("parameter", "My param value");
            params.setProperty("subject", "New Subject");
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setSender(sender);
            sendSmtpEmail.setTo(toList);

            sendSmtpEmail.setHtmlContent(email);
            sendSmtpEmail.setSubject(subject);
            sendSmtpEmail.setReplyTo(replyTo);
            sendSmtpEmail.setHeaders(headers);
            sendSmtpEmail.setParams(params);


            CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);
            System.out.println(response.toString());
        } catch (Exception e) {
            System.out.println("Exception occurred:- " + e.getMessage());
        }
    }

    @Override
    @Async
    public void receive(String source, String name, String subject) {
        String API_KEY = "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";

        ApiClient defaultClient = Configuration.getDefaultApiClient();
        // Configure API key authorization: api-key
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey(API_KEY);

        try {
            TransactionalEmailsApi api = new TransactionalEmailsApi();
            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail(source);
            sender.setName(name);
            List<SendSmtpEmailTo> toList = new ArrayList<SendSmtpEmailTo>();
            SendSmtpEmailTo to = new SendSmtpEmailTo();
            to.setEmail("mael.pilon-joly@hotmail.com");
            toList.add(to);
            SendSmtpEmailReplyTo replyTo = new SendSmtpEmailReplyTo();
            replyTo.setEmail(source);
            replyTo.setName(name);
            Properties headers = new Properties();
            headers.setProperty("Some-Custom-Name", "unique-id-1234");
            Properties params = new Properties();
            params.setProperty("parameter", "My param value");
            params.setProperty("subject", "New Subject");
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setSender(sender);
            sendSmtpEmail.setTo(toList);

            sendSmtpEmail.setHtmlContent(subject);
            sendSmtpEmail.setSubject("Alogplus customer email");
            sendSmtpEmail.setReplyTo(replyTo);
            sendSmtpEmail.setHeaders(headers);
            sendSmtpEmail.setParams(params);


            CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);
            System.out.println(response.toString());
        } catch (Exception e) {
            System.out.println("Exception occurred:- " + e.getMessage());
        }
    }

    @Override
    @Async
    public void messageBetweenUser(String username, String source, String dest, String subject) {
        String API_KEY = "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";

        ApiClient defaultClient = Configuration.getDefaultApiClient();
        // Configure API key authorization: api-key
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey(API_KEY);

        try {
            TransactionalEmailsApi api = new TransactionalEmailsApi();
            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail(source);
            sender.setName(username);
            List<SendSmtpEmailTo> toList = new ArrayList<SendSmtpEmailTo>();
            SendSmtpEmailTo to = new SendSmtpEmailTo();
            to.setEmail(dest);
            toList.add(to);
            SendSmtpEmailReplyTo replyTo = new SendSmtpEmailReplyTo();
            replyTo.setEmail(source);
            replyTo.setName(username);
            Properties headers = new Properties();
            headers.setProperty("Some-Custom-Name", "unique-id-1234");
            Properties params = new Properties();
            params.setProperty("parameter", "My param value");
            params.setProperty("subject", "New Subject");
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setSender(sender);
            sendSmtpEmail.setTo(toList);

            sendSmtpEmail.setHtmlContent(subject);
            sendSmtpEmail.setSubject("You received a message from a AlgoPlus user!");
            sendSmtpEmail.setReplyTo(replyTo);
            sendSmtpEmail.setHeaders(headers);
            sendSmtpEmail.setParams(params);


            CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);
            System.out.println(response.toString());
        } catch (Exception e) {
            System.out.println("Exception occurred:- " + e.getMessage());
        }
    }
}
