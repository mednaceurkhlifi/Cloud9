package tn.cloudnine.queute.serviceImpl.roadmap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import tn.cloudnine.queute.model.roadmap.RoadMap;


@Service
public class RoadMapGeminiService {
    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    public String generateContent(String prompt) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

        // Create request body
        String requestJson = """
            {
              "contents": [{
                "parts":[{"text": "%s"}]
              }]
            }
            """.formatted(prompt);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Make the request
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(requestJson, headers),
                String.class
        );

        return response.getBody();
    }



    public String clarifyText(String text) throws JsonProcessingException {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }

        String prompt = """
            Please reformat this text to be more readable while:
            1. Keeping all technical terms and proper names unchanged
            2. Only adding spaces between words where needed
            3. Returning ONLY the reformatted text without any additional commentary
            
            Text to reformat: "%s"
            """.formatted(text);

        String requestBody = String.format("""
            {
              "contents": [{
                "parts": [{"text": "%s"}]
              }]
            }
            """, prompt.replace("\"", "\\\""));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey,
                HttpMethod.POST,
                new HttpEntity<>(requestBody, headers),
                String.class
        );

        return extractCleanText(response.getBody());
    }

    private String extractCleanText(String geminiResponse) {
        try {
            JsonNode root = objectMapper.readTree(geminiResponse);
            return root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText()
                    .replace("\"", ""); // Remove any quotation marks
        } catch (Exception e) {
            return geminiResponse; // fallback
        }
    }



}
