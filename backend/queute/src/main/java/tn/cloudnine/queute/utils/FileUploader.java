package tn.cloudnine.queute.utils;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;

@Service
public class FileUploader implements IFileUploader{

    @Value("${file.upload.path}")
    private String fileUploadPath;

    public String saveImage(@Nonnull MultipartFile sourceFile) {
        final String fileUploadSubPath = "images" + separator;
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
