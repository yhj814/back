package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.AuditionApplicationFileDTO;
import com.app.ggumteo.domain.file.AuditionApplicationFileVO;
import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.repository.file.AuditionApplicationFileDAO;
import com.app.ggumteo.repository.file.FileDAO;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AuditionApplicationFileServiceImpl implements AuditionApplicationFileService {

    private final FileDAO fileDAO;
    private final AuditionApplicationFileDAO auditionApplicationFileDAO;

    @Override
    public void saveAuditionApplicationFile(AuditionApplicationFileVO auditionApplicationFileVO) {
        auditionApplicationFileDAO.insertAuditionApplicationFile(auditionApplicationFileVO);
    }

    // AuditionApplicationFileServiceImpl 코드 수정
    @Override
    public FileVO saveFile(MultipartFile file, Long auditionApplicationId) {
        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String relativePath = getPath() + "/" + uniqueFileName;
        String rootPath = "C:/upload/";

        FileVO fileVO = new FileVO();
        fileVO.setFileName(uniqueFileName);
        fileVO.setFileSize(String.valueOf(file.getSize()));
        fileVO.setFileType(file.getContentType());
        fileVO.setFilePath(relativePath);

        File saveLocation = new File(rootPath + relativePath);
        try {
            if (!saveLocation.getParentFile().exists()) {
                saveLocation.getParentFile().mkdirs();
            }
            file.transferTo(saveLocation);

            if (file.getContentType() != null && file.getContentType().startsWith("image")) {
                File thumbnailFile = new File(rootPath + getPath() + "/t_" + uniqueFileName);
                try (FileOutputStream thumbnail = new FileOutputStream(thumbnailFile)) {
                    Thumbnailator.createThumbnail(file.getInputStream(), thumbnail, 100, 100);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        // 파일 데이터 저장
        fileDAO.save(fileVO);

        // 오디션 신청 파일 데이터 저장
        AuditionApplicationFileVO auditionApplicationFileVO = new AuditionApplicationFileVO(fileVO.getId(), auditionApplicationId);
        auditionApplicationFileDAO.insertAuditionApplicationFile(auditionApplicationFileVO);

        return fileVO;
    }

    private String getPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }


    @Override
    public List<AuditionApplicationFileDTO> uploadFile(List<MultipartFile> files) throws IOException {
        List<AuditionApplicationFileDTO> fileDTOs = new ArrayList<>();

        String rootPath = "C:/upload/" + getPath();
        File directory = new File(rootPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (MultipartFile file : files) {
            UUID uuid = UUID.randomUUID();
            String uniqueFileName = uuid.toString() + "_" + file.getOriginalFilename();
            String relativePath = getPath() + "/" + uniqueFileName;

            AuditionApplicationFileDTO applyFileDTO = new AuditionApplicationFileDTO();
            applyFileDTO.setFileName(uniqueFileName);
            applyFileDTO.setFilePath(relativePath);
            applyFileDTO.setFileType(file.getContentType());
            applyFileDTO.setFileSize(String.valueOf(file.getSize()));

            File saveLocation = new File(rootPath, uniqueFileName);
            file.transferTo(saveLocation);

            if (file.getContentType() != null && file.getContentType().startsWith("image")) {
                try (FileOutputStream thumbnail = new FileOutputStream(new File(rootPath, "t_" + uniqueFileName))) {
                    Thumbnailator.createThumbnail(file.getInputStream(), thumbnail, 100, 100);
                }
            }
            fileDTOs.add(applyFileDTO);
        }

        return fileDTOs;
    }
}
