package user_service.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService{

    private final Path root = Paths.get("uploads/profile-photos");

    @Override
    public String upload(MultipartFile file) {
        try{
            if(Files.exists(root)){
                Files.createDirectories(root);
            }
            String fileName = UUID.randomUUID().toString()+ "_"+file.getOriginalFilename();
            return "/uploads/profile-photo/"+fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
