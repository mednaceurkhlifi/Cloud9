package tn.cloudnine.queute.utils;

import jakarta.annotation.Nonnull;
import org.springframework.web.multipart.MultipartFile;

public interface IFileUploader {
    String saveImage(@Nonnull MultipartFile sourceFile);
    boolean deleteFile(String filePath);
}
