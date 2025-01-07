package com.loan.management.loanmanagement_api.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class ApiGatewayController {

    private final String backendUrl = "http://localhost:8080"; // Backend Base URL
    private final RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/**")
    public ResponseEntity<?> routeRequests(HttpServletRequest request, @RequestBody(required = false) String body) {
        try {
            // Extract and decode query parameters
            String queryParams = request.getQueryString() != null
                    ? "?" + URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8)
                    : "";

            // Build target URL with decoded query parameters
            String targetUrl = backendUrl + request.getRequestURI().replace("/api", "") + queryParams;

            // Log the target URL for debugging
            System.out.println("Forwarding request to: " + targetUrl);

            // Extract HTTP method and headers
            HttpMethod method = HttpMethod.valueOf(request.getMethod());
            HttpHeaders headers = new HttpHeaders();
            Collections.list(request.getHeaderNames())
                .forEach(headerName -> headers.add(headerName, request.getHeader(headerName)));

            // Create HTTP entity with body and headers
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            // Forward the request to the backend
            ResponseEntity<String> response = restTemplate.exchange(targetUrl, method, entity, String.class);

            // Return the backend response
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            e.printStackTrace(); // Log any errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error forwarding request");
        }
    }
}
