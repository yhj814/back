package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.FileVO;

import com.app.ggumteo.domain.file.ProfileFileVO;
import com.app.ggumteo.repository.file.FileDAO;

import com.app.ggumteo.repository.file.ProfileFileDAO;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ProfileFileServiceImpl implements ProfileFileService {

    private final FileDAO fileDAO;  // 파일 정보 저장용
    private final ProfileFileDAO ProfileFileDAO;  // 파일과 게시물 관계 저장용

    @Override
    public void saveProfileFile(ProfileFileVO profileFileVO) {

       ProfileFileDAO.insertProfileFile(profileFileVO);
    }

    @Override
    public FileVO saveProFile(MultipartFile file, Long memberProfileId) {
        // 파일 정보를 담을 객체 생성
        FileVO fileVO = new FileVO();
        fileVO.setFileName(file.getOriginalFilename());
        fileVO.setFileSize(String.valueOf(file.getSize()));
        fileVO.setFileType(file.getContentType());
        fileVO.setFilePath("C:/ggumteofile/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename());

        // 파일 저장 로직
        File saveLocation = new File(fileVO.getFilePath());
        try {
            if (!saveLocation.getParentFile().exists()) {
                saveLocation.getParentFile().mkdirs();
            }
            file.transferTo(saveLocation);  // 실제 파일을 해당 경로에 저장
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        // tbl_file에 파일 정보 저장
        fileDAO.save(fileVO);  // 파일 정보를 tbl_file 테이블에 저장하고, 자동 생성된 ID를 가져옴


        ProfileFileVO profileFileVO = new ProfileFileVO(fileVO.getId(), memberProfileId);  // 파일 ID와 게시물 ID로 관계 생성
       ProfileFileDAO.insertProfileFile(profileFileVO);

        return fileVO;
    }
}
