package org.example.recruitmentsystem.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String uploadFile(
            MultipartFile file,
            String folder
    );

    String getFileUrl(String objectName);

    void deleteFile(String objectName);
}