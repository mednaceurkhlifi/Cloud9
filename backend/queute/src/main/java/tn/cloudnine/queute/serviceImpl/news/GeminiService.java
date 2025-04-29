package tn.cloudnine.queute.serviceImpl.news;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import tn.cloudnine.queute.service.news.IHuggingFaceImageService;

import java.util.*;

@Service
public class GeminiService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    IHuggingFaceImageService huggingFaceImageService;

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    public List<String> generateImagesForDescription(String description, int numImages) {
        return huggingFaceImageService.generateImagesForDescription(description, numImages);
    }

    public String generateContent(String description) {

        List<String> imageUrls = generateImagesForDescription(description, 2); // Generate up to 5 images


        // Construct the prompt
        /*String prompt = String.format(
                "Generate a detailed, engaging article about: **%s**. Return a **strictly valid Tiptap v2 JSON** with proper formatting (headings, bold, lists, etc.) and **only working images**.\n\n" +

                        "### **IMAGE REQUIREMENTS (STRICT):**\n" +
                        "- **URLs MUST:**\n" +
                        "From this website 'https://www.istockphoto.com/' or 'https://www.pexels.com/' or 'https://www.freepik.com/'"+
                        "and the structure of the image must folow this'https://media.istockphoto.com/id/1173935107/photo/long-wave-on-the-coast-dawn-on-the-sea-tunisia.jpg?s=2048x2048&w=is&k=20&c=Z4p6ds-FoAD-a71j_wtMpSocRt8_fN_rQIp8ARgEH8U='"+

                        "### **TIPTAP FORMATTING (MUST INCLUDE):**\n" +
                        "- Use **headings** (`\"type\": \"heading\"` with `\"attrs\": {\"level\": 1|2|3}`)\n" +
                        "- Include **bold** (`{\"type\": \"strong\"}`), *italic* (`{\"type\": \"italic\"}`), <u>underline</u> (`{\"type\": \"underline\"}`) via the `\"marks\"` array within text nodes.\n" +
                        "- Use `{\"type\": \"blockquote\"}`, `{\"type\": \"codeBlock\"}`, `{\"type\": \"bulletList\"}` (with `{\"type\": \"listItem\"}` children containing `{\"type\": \"paragraph\"}`) and `{\"type\": \"orderedList\"}` where relevant.\n" +
                        "- For images, use **exactly this structure** (no extra fields):\n" +
                        "\n" +
                        "{\n" +
                        "  \"type\": \"image\",\n" +
                        "  \"attrs\": {\n" +
                        "    \"src\": \"https://example.com/valid-image.jpg\",\n" +
                        "    \"alt\": \"Clear description of the image\",\n" +
                        "    \"class\": \"responsive-image\"\n" +
                        "  }\n" +
                        "}\n" +
                        "\n" +
                        "- **Final output MUST be pure JSON** (no extra text/comments).\n\n" +

                        "### **ARTICLE STRUCTURE:**\n" +
                        "- **Introduction** (compelling opener)\n" +
                        "- **3-5 detailed sections** (with subheadings)\n" +
                        "- **Conclusion** (summary/call-to-action)\n\n" +

                        "Return **ONLY** the raw Tiptap JSON, like this:\n" +

                        "{\n" +
                        "  \"type\": \"doc\",\n" +
                        "  \"content\": [\n" +
                        "    // Your nodes here...\n" +
                        "  ]\n" +
                        "}\n"
                ,
                description
        );*/
        String prompt = String.format(
                "Generate a detailed, engaging article about: **%s**. Return a **strictly valid Tiptap v2 JSON** with proper formatting (headings, bold, lists, etc.) and **only working images**.\n\n" +
                        "### **TIPTAP FORMATTING (MUST INCLUDE):**\n" +
                        "- Use **headings** (`\"type\": \"heading\"` with `\"attrs\": {\"level\": 1|2|3}`)\n" +
                        "- Include **bold** (`{\"type\": \"bold\"}`), *italic* (`{\"type\": \"italic\"}`), <u>underline</u> (`{\"type\": \"underline\"}`) via the `\"marks\"` array within text nodes.\n" +
                        "- Use `{\"type\": \"blockquote\"}`, `{\"type\": \"codeBlock\"}`, `{\"type\": \"bulletList\"}` (with `{\"type\": \"listItem\"}` children containing `{\"type\": \"paragraph\"}`) and `{\"type\": \"orderedList\"}` where relevant.\n" +
                        "- For images, use **exactly this structure** (no extra fields):\n\n" +
                        "{\n  \"type\": \"image\",\n  \"attrs\": {\n    \"src\": \"https://example.com/valid-image.jpg\",\n    \"alt\": \"Clear description of the image\",\n    \"class\": \"responsive-image\"\n  }\n}\n\n" +
                        "- **Final output MUST be pure JSON** (no extra text/comments).\n\n" +
                        "### **ARTICLE STRUCTURE:**\n" +
                        "- **Introduction** (compelling opener)\n" +
                        "- **3-5 detailed sections** (with subheadings)\n" +
                        "- **Conclusion** (summary/call-to-action)\n\n" +
                        "Return **ONLY** the raw Tiptap JSON, like this:\n\n" +
                        "{\n  \"type\": \"doc\",\n  \"content\": [\n    // Your nodes here...\n  ]\n}\n\n" +
                        "### **IMAGE REQUIREMENTS (STRICT):**\n" +
                        "- **URLs MUST** be selected from the following list of images generated for your topic:\n" +
                        "%s\n" +  // This is the only additional placeholder needed
                        "- Each image should be selected to match the section or paragraph it is used for.\n" +
                        "- Ensure that images are relevant to the context of the content.\n" +
                        "- Only use images from the list provided above.",
                description,
                String.join("\n", imageUrls.stream()
                        .map(url -> String.format("  - \"%s\"", url))
                        .toArray(String[]::new))
        );




        // Prepare the Gemini API request body
        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(part));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));

        // Add headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        String urlWithApiKey = GEMINI_API_URL + "?key=" + apiKey;

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(urlWithApiKey, entity, String.class);
            String responseBody = response.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            JsonNode contentNode = rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text");
            String jsonOutput = contentNode.asText();

            // Strip markdown-style JSON block markers if present
            if (jsonOutput.startsWith("```json") && jsonOutput.endsWith("```")) {
                jsonOutput = jsonOutput.substring(7, jsonOutput.length() - 3);
            } else if (jsonOutput.startsWith("```") && jsonOutput.endsWith("```")) {
                jsonOutput = jsonOutput.substring(3, jsonOutput.length() - 3);
            }
            return jsonOutput;

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return "Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
