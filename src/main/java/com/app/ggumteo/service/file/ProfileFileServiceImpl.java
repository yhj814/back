package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.ProfileFileDTO;
import com.app.ggumteo.domain.file.ProfileFileVO;
import com.app.ggumteo.repository.file.FileDAO;
import com.app.ggumteo.repository.file.ProfileFileDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ProfileFileServiceImpl implements ProfileFileService {

    private final FileDAO fileDAO;
    private final ProfileFileDAO profileFileDAO;

    @Override
    public void saveProfileFile(ProfileFileVO profileFileVO) {
        profileFileDAO.insertProfileFile(profileFileVO);
    }

    @Override
    public ProfileFileDTO saveProFile(MultipartFile file, Long memberProfileId) {
        // 파일 정보 저장 준비
        FileVO fileVO = new FileVO();
        fileVO.setFileName(file.getOriginalFilename());
        fileVO.setFileSize(String.valueOf(file.getSize()));
        fileVO.setFileType(file.getContentType());
        fileVO.setFilePath("C:/ggumteofile/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename());

        // 파일 저장
        File saveLocation = new File(fileVO.getFilePath());
        try {
            if (!saveLocation.getParentFile().exists()) {
                saveLocation.getParentFile().mkdirs();
            }
            file.transferTo(saveLocation);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        // 파일 정보를 tbl_file에 저장
        fileDAO.save(fileVO);

        // 관계 테이블에 memberProfileId와 저장된 fileVO의 id를 사용하여 ProfileFileVO 저장
        ProfileFileVO profileFileVO = new ProfileFileVO(fileVO.getId(), memberProfileId);
        profileFileDAO.insertProfileFile(profileFileVO);

        // 필요한 파일 정보로 ProfileFileDTO 반환
        return new ProfileFileDTO(fileVO.getId(), fileVO.getFileName(), fileVO.getFilePath());
    }
}
