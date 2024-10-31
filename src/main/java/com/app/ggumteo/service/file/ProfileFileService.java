package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.ProfileFileDTO;
import com.app.ggumteo.domain.file.ProfileFileVO;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileFileService {
    void saveProfileFile(ProfileFileVO profileFileVO);
    // 변경: MultipartFile을 받아서 ProfileFileDTO를 반환하는 메서드
    ProfileFileDTO saveProFile(MultipartFile file, Long memberProfileId);
}
