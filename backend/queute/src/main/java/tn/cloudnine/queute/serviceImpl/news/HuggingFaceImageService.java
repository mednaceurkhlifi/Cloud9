package tn.cloudnine.queute.serviceImpl.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import tn.cloudnine.queute.service.news.IHuggingFaceImageService;
import tn.cloudnine.queute.utils.IFileUploader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class HuggingFaceImageService implements IHuggingFaceImageService {

    @Autowired
    private IFileUploader fileUploader;

    @Value("${huggingface.api.key}")
    private String apiKey;

    @Value("${imgbb.api.key}")  // External API key for imgbb
    private String imgbbApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    // Converts a base64 string to a MultipartFile
    public MultipartFile base64ToMultipart(String base64, String filename) {
        byte[] imageBytes = Base64.getDecoder().decode(base64);
        return new MockMultipartFile(filename, filename, "image/jpeg", imageBytes);
    }

    // Generate image from HuggingFace model and return Base64-encoded image
    public String generateImageBase64(String prompt) {
        try {
            String apiUrl = "https://api-inference.huggingface.co/models/stabilityai/stable-diffusion-xl-base-1.0";

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("inputs", prompt);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("Content-Type", "application/json");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    byte[].class
            );

            byte[] imageBytes = response.getBody();
            if (imageBytes == null) return null;

            return Base64.getEncoder().encodeToString(imageBytes);

        } catch (Exception e) {
            System.out.println("Error generating image from HuggingFace API: " + e.getMessage());
            return null;
        }
    }

    // Generate and store image, return the external URL
    public String generateAndStoreImage(String prompt) {
        String base64 = generateImageBase64(prompt);
        if (base64 == null) return null;

        MultipartFile file = base64ToMultipart(base64, System.currentTimeMillis() + "_ai.jpg");
        String externalImageUrl = uploadToExternalService(file);

        return externalImageUrl;  // Returning the URL of the uploaded image
    }

    // Generate multiple images and return their URLs
    public List<String> generateImagesForDescription(String description, int numImages) {
        List<String> imageUrls = new ArrayList<>();

        for (int i = 0; i < numImages; i++) {
            String imageUrl = generateAndStoreImage(description + " " + (i + 1));
            if (imageUrl != null) {
                imageUrls.add(imageUrl);
            }
        }

        return imageUrls;
    }

    // Upload image to imgBB external image hosting service
    private String uploadToExternalService(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            System.out.println("Attempted to upload null or empty file");
            return null;
        }

        String apiUrl = "https://api.imgbb.com/1/upload?key=" + imgbbApiKey; // Image upload endpoint

        try {
            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Create temporary file to avoid memory issues with large files
            Path tempFile = Files.createTempFile("upload_", ".tmp");
            try {
                file.transferTo(tempFile);

                // Prepare request body
                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("image", new FileSystemResource(tempFile.toFile()));

                // Create request entity
                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

                // Execute request with timeout
                ResponseEntity<String> response = restTemplate.exchange(
                        apiUrl,
                        HttpMethod.POST,
                        requestEntity,
                        String.class
                );

                // Verify response
                if (!response.getStatusCode().equals(HttpStatus.OK)) {
                    System.out.println("External service returned non-success status: " + response.getStatusCode());
                    return null;
                }

                // Parse response
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                if (!rootNode.has("data") || !rootNode.path("data").has("url")) {
                    System.out.println("Response missing required 'url' field: " + response.getBody());
                    return null;
                }

                return rootNode.path("data").path("url").asText();

            } finally {
                // Clean up temporary file
                Files.deleteIfExists(tempFile);
            }

        } catch (ResourceAccessException e) {
            System.out.println("Connection to external service failed: " + e.getMessage());
            return null;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.out.println("External service error - Status: " + e.getStatusCode() + ", Body: " + e.getResponseBodyAsString());
            return null;
        } catch (IOException e) {
            System.out.println("File handling error: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Unexpected error during upload: " + e.getMessage());
            return null;
        }
    }


}
