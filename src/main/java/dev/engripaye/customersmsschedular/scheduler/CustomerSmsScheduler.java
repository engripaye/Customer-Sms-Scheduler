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

        for (List<Object> customer : customers) {
            if (customer.size() >= 2) { // Name & contact must exist
                String name = customer.get(0).toString();
                String phone = customer.get(1).toString();

                String message = String.format("Hello" + name + ", thank you for shopping with us today! We appreciate your feedback. Hope to see you again on your next shopping.");
                smsService.sendSms(phone, message);

            }
        }
    }catch (Exception e){
        System.err.println("Error sending Sms: " + e.getMessage());
    }

    }
}
