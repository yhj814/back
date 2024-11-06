package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.AuditionApplicationFileDTO;
import com.app.ggumteo.domain.file.AuditionApplicationFileVO;
import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.repository.file.AuditionApplicationFileDAO;
import com.app.ggumteo.repository.file.FileDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditionApplicationFileServiceImpl implements AuditionApplicationFileService {

    private final FileDAO fileDAO;
    private final AuditionApplicationFileDAO auditionApplicationFileDAO;

    @Override
    public void saveAuditionApplicationFile(AuditionApplicationFileVO auditionApplicationFileVO) {
        auditionApplicationFileDAO.insertAuditionApplicationFile(auditionApplicationFileVO);
    }

    @Override
    public AuditionApplicationFileDTO saveAuditionApplicationFile(MultipartFile file, Long auditionApplicationId) {
        FileVO fileVO = new FileVO();
        fileVO.setFileName(file.getOriginalFilename());
        fileVO.setFileSize(String.valueOf(file.getSize()));
        fileVO.setFileType(file.getContentType());
        fileVO.setFilePath("C:/ggumteofile/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename());

        File saveLocation = new File(fileVO.getFilePath());
        try {
            if (!saveLocation.getParentFile().exists()) {
                saveLocation.getParentFile().mkdirs();
            }
            file.transferTo(saveLocation);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        fileDAO.save(fileVO);

        AuditionApplicationFileVO auditionApplicationFileVO = new AuditionApplicationFileVO(fileVO.getId(), auditionApplicationId);
        auditionApplicationFileDAO.insertAuditionApplicationFile(auditionApplicationFileVO);

        return new AuditionApplicationFileDTO(fileVO.getId(), fileVO.getFileName(), fileVO.getFilePath());
    }
}
