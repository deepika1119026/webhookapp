package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class WebhookappApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WebhookappApplication.class, args);
    }

    @Override
    public void run(String... args) {
        generateWebhook();
    }

    private void generateWebhook() {

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        Map<String, String> request = new HashMap<>();
        request.put("name", "Deepika Vishwakarma");
        request.put("regNo", "9090909090");
        request.put("email", "abc@gmail.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        String webhookUrl = (String) response.getBody().get("webhook");
        String accessToken = (String) response.getBody().get("accessToken");

        System.out.println("Webhook URL: " + webhookUrl);
        System.out.println("Access Token: " + accessToken);

        String finalQuery = getFinalSqlQuery();

        sendAnswer(restTemplate, webhookUrl, accessToken, finalQuery);
    }

    private String getFinalSqlQuery() {
        
        return "SELECT * FROM employees;";
    }

    private void sendAnswer(RestTemplate restTemplate, String webhookUrl, String token, String finalQuery) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        Map<String, String> body = new HashMap<>();
        body.put("finalQuery", finalQuery);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, request, String.class);

        System.out.println("Final Submission Response: " + response.getBody());
    }
}
