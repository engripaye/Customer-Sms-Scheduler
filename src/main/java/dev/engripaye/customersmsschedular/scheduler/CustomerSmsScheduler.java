package dev.engripaye.customersmsschedular.scheduler;


import dev.engripaye.customersmsschedular.service.GoogleSheetService;
import dev.engripaye.customersmsschedular.service.SmsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerSmsScheduler {

    private final GoogleSheetService sheetService;
    private final SmsService smsService;

    public CustomerSmsScheduler(GoogleSheetService sheetService, SmsService smsService) {
        this.sheetService = sheetService;
        this.smsService = smsService;
    }

    // Runs every day at 10 PM
    @Scheduled(cron = "0 0 22 * * ?")
    public void sendDailySms(){

    try {
        List<List<Object>> customers = sheetService.getAllCustomers();
        String today = java.time.LocalDateTime.now().toString();

        // Skip header row → start from row index 2
        for (int i = 1; i < customers.size(); i++) {
            List<Object> customer = customers.get(i);

            if(customer.size() < 2 ) continue;


            String name = customer.get(0).toString();
            String phone = customer.get(1).toString();

            // if there's a last sent date
            String lastSent = customer.size() >= 7? customer.get(6).toString() : "";

            // skip if already sent today
            if (today.equals(lastSent)){
                System.out.println("Skipping " + name + " - already notified today");
            }

            // SEND SMS
            String message = String.format("Hello" + name + ", thank you for shopping with us today! We appreciate your feedback. Hope to see you again on your next shopping.");
            smsService.sendSms(phone, message);

            // Update last sent in Google Sheet
            sheetService.updateLastSent(i + 1, today);
            System.out.println("SMS sent to " + name);

        }

    }catch (Exception e){
        System.err.println("Error sending Sms: " + e.getMessage());
    }

    }
}
