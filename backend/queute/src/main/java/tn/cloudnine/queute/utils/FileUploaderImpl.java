package tn.cloudnine.queute.utils;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploaderImpl implements IFileUploader {
    @Override
    public String saveImage(@Nonnull MultipartFile sourceFile) {
        try{
            String fileName = UUID.randomUUID() + "_" + sourceFile.getOriginalFilename();
            String uploadsDir = Paths.get("").toAbsolutePath().toString() + File.separator +"uploads\\images";
            Files.createDirectories(Paths.get(uploadsDir));
            String filePath =uploadsDir+File.separator + fileName;
            sourceFile.transferTo(new File(filePath));
            return fileName;

        }catch(IOException ex){
            System.err.println(ex.getMessage());
        }
        return "";
    }

    @Override
    public boolean deleteFile(String filePath) {
        try{
            String uploadsDir = Paths.get("").toAbsolutePath().toString() + filePath;
            if(Files.deleteIfExists(Paths.get(uploadsDir))){
                return true;
            }
            return false;

        }catch (IOException e){
            System.err.println("file does not exist");
            return false;
        }
    }
}
