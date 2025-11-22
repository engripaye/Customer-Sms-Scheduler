package dev.engripaye.customersmsschedular.service;

import com.google.api.client.util.Value;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
            body.put("type", "plain");
            body.put("channel", "generic");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            // Send SMS via Termii message
            restTemplate.postForObject(smsUrl, request, String.class);

            System.out.println("SMS sent to " + toNumber);

        }catch (Exception e){
            System.err.println("Failed to send SMS to " + toNumber);
            e.printStackTrace();
        }
    } // working on termii application setup : call service
}
