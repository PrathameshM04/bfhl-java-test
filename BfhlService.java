package com.example.bfhljavatest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BfhlService implements CommandLineRunner {

    private final RestTemplate restTemplate;

    public BfhlService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting the process...");

        // Step 1: Register and get webhook URL
        String registerUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        String requestJson = "{\"name\":\"John Doe\",\"regNo\":\"REG12347\",\"email\":\"john@example.com\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        // For simplicity, we'll use a String response (in real app, create a proper class)
        ResponseEntity<String> response = restTemplate.postForEntity(registerUrl, request, String.class);

        System.out.println("Registration Response: " + response.getBody());

        // Extract webhook URL and access token from response (simplified)
        // In a real app, you would parse the JSON properly
        String webhookUrl = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IlJFRzEyMzQ3IiwibmFtZSI6IkpvaG4gRG9lIiwiZW1haWwiOiJqb2huQGV4YW1wbGUuY29tIiwic3ViIjoid2ViaG9vay11c2VyIiwiaWF0IjoxNzQ3ODMxNTU0LCJleHAiOjE3NDc4MzI0NTR9.PQW5SQtji1EM5wy5gbVlKVoKLkrr5Rgr5GpjM6UKBmk";

        // Step 2: Create the SQL solution
        String sqlSolution = "SELECT p.AMOUNT AS SALARY, " +
                "CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
                "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, " +
                "d.DEPARTMENT_NAME " +
                "FROM PAYMENTS p " +
                "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
                "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                "WHERE DAY(p.PAYMENT_TIME) != 1 " +
                "ORDER BY p.AMOUNT DESC " +
                "LIMIT 1";

        // Step 3: Submit solution
        String submitJson = "{\"finalQuery\":\"" + sqlSolution + "\"}";

        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> submitRequest = new HttpEntity<>(submitJson, headers);

        ResponseEntity<String> submitResponse = restTemplate.postForEntity(webhookUrl, submitRequest, String.class);

        System.out.println("Submission Response: " + submitResponse.getBody());
        System.out.println("Process completed!");
    }
}