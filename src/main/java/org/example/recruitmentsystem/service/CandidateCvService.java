package org.example.recruitmentsystem.service;

import org.example.recruitmentsystem.dto.response.CandidateCvResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CandidateCvService {

    List<CandidateCvResponse> getMyCvs(String email);

    CandidateCvResponse uploadCv(
            String email,
            String title,
            MultipartFile file
    );

    CandidateCvResponse setDefaultCv(
            String email,
            Long cvId
    );

    void deleteCv(
            String email,
            Long cvId
    );
}