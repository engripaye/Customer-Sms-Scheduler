# 📩 Customer SMS Scheduler

### Automated Nightly SMS Sender Using Google Sheets + Twilio | Spring Boot | Scheduler

This project — **CustomerSmsScheduler** — is a Spring Boot application that automatically reads customer data from **Google Sheets** every night and sends each customer a personalized **SMS message** using **Twilio**.

It is designed for businesses that collect customer data in Google Sheets and want an automated, reliable way to send appreciation messages, follow-ups, or reminders — without manual effort.

---

## 🚀 Features

### ✅ 1. Automated Nightly SMS Scheduling

Runs every night at **10 PM** using Spring Scheduler (`@Scheduled` cron job).

### ✅ 2. Google Sheets Integration

Fetches customer records from a shared Google Sheet using:

* Google Sheets API
* Service Account authentication

### ✅ 3. Twilio SMS Sender

Sends personalized SMS messages to each customer with:

* Twilio REST API
* Configurable sender number and credentials

### ✅ 4. Fully Configurable

Environment variables let you customize:
✔ Google Sheet ID
✔ Sheet range
✔ Twilio SID, token, and phone number
✔ Cron expression

---

## 📂 Project Structure

```
src/main/java/dev/engripaye/customersmsscheduler/
│
├── CustomerSmsSchedulerApplication.java     # Main application entry point
│
├── scheduler/
│   └── CustomerSmsScheduler.java            # Nightly scheduler logic
│
├── service/
│   ├── GoogleSheetsService.java             # Reads data from Google Sheets
│   └── SmsService.java                      # Sends SMS using Twilio
│
└── resources/
    └── application.properties               # Environment configuration
    └── service_account.json                 # Google service account key
```

---

## 🛠️ Technology Stack

| Component         | Technology        |
| ----------------- | ----------------- |
| Backend Framework | Spring Boot 3.x   |
| Task Scheduling   | Spring Scheduler  |
| External API      | Google Sheets API |
| SMS Provider      | Twilio            |
| Build System      | Maven             |
| Java Version      | Java 17+          |

---

## 📦 Dependencies (pom.xml)

```xml
<dependencies>
    <!-- Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <!-- Google Sheets API -->
    <dependency>
        <groupId>com.google.apis</groupId>
        <artifactId>google-api-services-sheets</artifactId>
        <version>v4-rev20230815-2.0.0</version>
    </dependency>
    <dependency>
        <groupId>com.google.api-client</groupId>
        <artifactId>google-api-client</artifactId>
        <version>2.2.0</version>
    </dependency>
    <dependency>
        <groupId>com.google.oauth-client</groupId>
        <artifactId>google-oauth-client-jetty</artifactId>
        <version>2.2.0</version>
    </dependency>

    <!-- Twilio -->
    <dependency>
        <groupId>com.twilio.sdk</groupId>
        <artifactId>twilio</artifactId>
        <version>8.31.1</version>
    </dependency>
</dependencies>
```

---

## ⚙️ Configuration

### `application.properties`

```properties
# Google Sheet
google.sheet.id=YOUR_SPREADSHEET_ID
google.sheet.range=sheet1!A:F
google.service.account.key.path=src/main/resources/service_account.json

# Twilio SMS
twilio.account.sid=YOUR_TWILIO_ACCOUNT_SID
twilio.auth.token=YOUR_TWILIO_AUTH_TOKEN
twilio.phone.number=+1234567890
```

---

## 📑 Core Components

### 🔹 GoogleSheetsService — Fetch Customers from Google Sheets

```java
public List<List<Object>> getAllCustomers() throws Exception {
    FileInputStream stream = new FileInputStream(serviceAccountPath);
    ServiceAccountCredentials credentials = ServiceAccountCredentials.fromStream(stream);

    Sheets sheets = new Sheets.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            JacksonFactory.getDefaultInstance(),
            new HttpCredentialsAdapter(credentials)
    ).setApplicationName("Customer SMS Scheduler").build();

    ValueRange response = sheets.spreadsheets().values()
            .get(spreadsheetId, range)
            .execute();

    return response.getValues();
}
```

---

### 🔹 SmsService — Send SMS Using Twilio

```java
public void sendSms(String toNumber, String messageBody) {
    Twilio.init(accountSid, authToken);

    Message.creator(
            new PhoneNumber(toNumber),
            new PhoneNumber(fromNumber),
            messageBody
    ).create();
}
```

---

### 🔹 CustomerSmsScheduler — Run Daily at 10 PM

```java
@Scheduled(cron = "0 0 22 * * ?")
public void sendDailySms() {
    List<List<Object>> customers = sheetsService.getAllCustomers();

    for (List<Object> customer : customers) {
        if (customer.size() >= 2) {
            String name = customer.get(0).toString();
            String phone = customer.get(1).toString();
            String message = "Hi " + name + ", thank you for shopping with us! 🎉";

            smsService.sendSms(phone, message);
        }
    }
}
```

---

## ▶️ How to Run

### **1. Clone the project**

```bash
git clone https://github.com/yourname/CustomerSmsScheduler.git
cd CustomerSmsScheduler
```

### **2. Add your `service_account.json`**

Place your Google service account key in:

```
src/main/resources/service_account.json
```

### **3. Update `application.properties`**

Add your Google Sheet ID and Twilio credentials.

### **4. Run the application**

```bash
mvn spring-boot:run
```

---

## 🔐 Requirements

Before running, ensure that:

### ✔ Your Google Sheet is shared with your service account email

Format example:

| Name | Contact | Rating | Items Not Found | Price To Reduce | Suggestion |

### ✔ Your Twilio phone number is verified

or you’re using a paid number.

---

## 📆 Scheduler Timing

Current configuration:

```
0 0 22 * * ?
```

Meaning:

> Run every day at **10:00 PM**.

You can adjust the cron expression as needed.

---

## 🧰 Future Enhancements

* SMS templates with dynamic placeholders
* Admin dashboard for schedule management
* Multi-provider SMS support (Africa’s Talking, Termii, etc.)
* Retry mechanism for failed sends
* Store logs/history in PostgreSQL

---

## 🤝 Contributing

Pull requests are welcome!
For major changes, please open an issue first to discuss what you would like to change.

---

## 📝 License

This project is licensed under the **MIT License**.

---

