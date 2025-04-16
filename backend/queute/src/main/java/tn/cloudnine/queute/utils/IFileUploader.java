package tn.cloudnine.queute.utils;

import jakarta.annotation.Nonnull;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IFileUploader {
    String saveImage(@Nonnull MultipartFile sourceFile);
    String saveDocument(@Nonnull MultipartFile sourceFile);
    ResponseEntity<Resource> serveFile(String subFolder, String filename,
                                       String fallbackMediaType);
}