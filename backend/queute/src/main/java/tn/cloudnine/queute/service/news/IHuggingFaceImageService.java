package tn.cloudnine.queute.service.news;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IHuggingFaceImageService {
    public MultipartFile base64ToMultipart(String base64, String filename) ;
    public String generateImageBase64(String prompt);
    public String generateAndStoreImage(String prompt);
    public List<String> generateImagesForDescription(String description, int numImages);

    }
