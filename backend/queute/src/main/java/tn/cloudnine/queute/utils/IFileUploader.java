package tn.cloudnine.queute.utils;

import jakarta.annotation.Nonnull;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IFileUploader {
    String saveImage(@Nonnull MultipartFile sourceFile);
    boolean deleteFile(String filePath);
    ResponseEntity<Resource> serveFile(String subFolder, String filename,
                                       String fallbackMediaType);
}
