package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.FileVO;

import com.app.ggumteo.domain.file.ProfileFileVO;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileFileService {
    void saveProfileFile(ProfileFileVO profileFileVO);
    // MultipartFile을 받아서 FileVO를 반환하는 메서드
    FileVO saveProFile(MultipartFile file, Long memberProfileId);
}