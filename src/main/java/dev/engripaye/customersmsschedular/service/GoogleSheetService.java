package dev.engripaye.customersmsschedular.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.List;

@Service
public class GoogleSheetService {

    @Value("${google.sheet.id}")
    private String spreadsheetId;

    @Value("${google.sheet.range}")
    private String range;

    @Value("${google.credentials.file.path}")
    private String serviceAccountPath;


    public List<List<Object>> getAllCustomers() throws Exception {
        FileInputStream serviceAccountStream = new FileInputStream(serviceAccountPath);

        ServiceAccountCredentials credentials =
                ServiceAccountCredentials.fromStream(serviceAccountStream);

        Sheets sheetsService = new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("Customer SMS Scheduler")
                .build();

        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        return response.getValues();
    }


    public void updateLastSent(int rowIndex, String date) throws Exception{
        FileInputStream serviceAccountStream = new FileInputStream(serviceAccountPath);
        ServiceAccountCredentials credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
        Sheets sheetService = new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("Customer Sms Scheduler").build();

        ValueRange body = new ValueRange().setValues(
                List.of(List.of(date))
        );

        // "G" column = Last Sent
        String cell = "sheet1!G" + rowIndex;

        sheetService.spreadsheets().values()
                .update(spreadsheetId, cell, body)
                .setValueInputOption("USER_ENTERED")
                .execute();

    } // setting up google service for deployment
}
