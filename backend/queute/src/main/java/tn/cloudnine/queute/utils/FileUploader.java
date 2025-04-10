package tn.cloudnine.queute.utils;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;

import static java.io.File.separator;

@Service
public class FileUploader implements IFileUploader{

    @Value("${file.upload.path}")
    private String fileUploadPath;

    public ResponseEntity<Resource> serveFile(String subFolder, String filename,
                                              String fallbackMediaType) {
        try {
            Path filePath = Paths.get(fileUploadPath + File.separator + subFolder).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String mimeType = Files.probeContentType(filePath);
            MediaType mediaType = mimeType != null
                    ? MediaType.parseMediaType(mimeType)
                    : (fallbackMediaType != null
                    ? MediaType.parseMediaType(fallbackMediaType)
                    : MediaType.APPLICATION_OCTET_STREAM);

            boolean isInline = mediaType.toString().startsWith("image/") || mediaType.toString().startsWith("text/");

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            (isInline ? "inline" : "attachment") + "; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public String saveImage(@Nonnull MultipartFile sourceFile) {
        final String fileUploadSubPath = "images" + separator;
        return uploadFile(sourceFile, fileUploadSubPath);
    }

        public String saveDocument(@Nonnull MultipartFile sourceFile) {
        final String fileUploadSubPath = "documents" + separator;
        return uploadFile(sourceFile, fileUploadSubPath);
    }

    private String uploadFile(@Nonnull MultipartFile sourceFile, @Nonnull String fileUploadSubPath) {
        final String finalUploadPath = fileUploadPath + separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);

        if(!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdirs();
            if(!folderCreated) {
                return null; // Exception will be treated later
            }
        }

        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        final String fileName = System.currentTimeMillis() + "." + fileExtension;
        String targetFilePath = finalUploadPath + separator + fileName;
        Path targetPath = Paths.get(targetFilePath);

        try{
            Files.write(targetPath, sourceFile.getBytes());
            return  fileName;
        } catch (IOException e) {
            return "UNKNOWN"; // Exception will be treated later
        }
    }

    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }

    private String getFileExtension(String originalFilename) {
        if(originalFilename == null || originalFilename.isEmpty()){
            return "";
        }
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if(lastDotIndex == -1)
            return "";
        return originalFilename.substring(lastDotIndex +1).toLowerCase();
    }
}
