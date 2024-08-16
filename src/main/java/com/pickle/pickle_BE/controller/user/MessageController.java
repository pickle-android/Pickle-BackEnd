package com.pickle.pickle_BE.controller.user;

import com.pickle.pickle_BE.entity.User;
import com.pickle.pickle_BE.service.UserService;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

@Component
@RestController
public class MessageController {

    @Autowired
    private UserService userService;

    final DefaultMessageService messageService;
    private String senderPhoneNumber;


    @Autowired
    public MessageController(UserService userService,
                             @Value("${coolsms.api.key}") String key,
                             @Value("${coolsms.api.secret}") String secret,
                             @Value("${coolsms.api.number}") String senderPhoneNumber) {
        this.userService = userService;
        this.senderPhoneNumber = senderPhoneNumber;
        this.messageService = NurigoApp.INSTANCE.initialize(key, secret, "https://api.coolsms.co.kr");
    }

    public static String generateRandomStr(int length, boolean isUpperCase) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return isUpperCase ? sb.toString() : sb.toString().toLowerCase();
    }

    @PutMapping("/users/password")
    public SingleMessageSentResponse sendOne(@RequestParam String phoneNumber, String email) {
        User user = userService.getUserByPhoneNumberAndEmail(phoneNumber, email);
        if (user == null) {
            throw new IllegalArgumentException("There is no user");
        }
        Message message = new Message();

        String temppwd=generateRandomStr(8, true);
        userService.updatePassword(email,temppwd);

        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력
        message.setFrom(senderPhoneNumber);
        message.setTo(phoneNumber);
        message.setText("임시 비밀번호는 [  " + temppwd + "  ] 입니다.");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return response;
    }
}