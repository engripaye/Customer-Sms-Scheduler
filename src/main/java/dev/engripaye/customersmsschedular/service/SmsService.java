package dev.engripaye.customersmsschedular.service;

import com.google.api.client.util.Value;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsService {

    @Value("${termii.account.sid}")
    private String apiKey;

    @Value("${termii.auth.token}")
    private String senderId;

    @Value("${termii.phone.number}")
    private String smsUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    public void sendSms(String toNumber, String messageBody){

        try {
            // Build request payload
            Map<String, Object> body = new HashMap<>();
            body.put("api_key", apiKey);
            body.put("from", senderId);
            body.put("to", toNumber);
            body.put("sms", messageBody);
            body.put("")

        }
    }
}
