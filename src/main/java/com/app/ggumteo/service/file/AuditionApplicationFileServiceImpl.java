package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.AuditionApplicationFileDTO;
import com.app.ggumteo.domain.file.AuditionApplicationFileVO;
import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.repository.audition.AuditionDAO;
import com.app.ggumteo.repository.file.AuditionApplicationFileDAO;
import com.app.ggumteo.repository.file.FileDAO;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
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
    private final AuditionDAO auditionDAO;

    @Override
    public void saveAuditionApplicationFile(AuditionApplicationFileVO auditionApplicationFileVO) {
        auditionApplicationFileDAO.insertAuditionApplicationFile(auditionApplicationFileVO);
    }

    // AuditionApplicationFileServiceImpl 코드 수정
    @Override
    public FileVO saveFile(MultipartFile file) {
        String rootPath = "C:/upload/" + getPath() + "/";
        File directory = new File(rootPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        File saveLocation = new File(rootPath + uniqueFileName);
        try {
            file.transferTo(saveLocation);

            // 이미지 파일인 경우 썸네일 생성
            if (file.getContentType() != null && file.getContentType().startsWith("image")) {
                String thumbnailFileName = "t_" + uniqueFileName;
                File thumbnailFile = new File(rootPath + thumbnailFileName);

                // 썸네일 이미지 생성 (예: 가로 100px, 세로 비율에 맞게)
                Thumbnails.of(saveLocation)
                        .size(100, 100)
                        .toFile(thumbnailFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        FileVO fileVO = new FileVO();
        fileVO.setFileName(uniqueFileName);
        fileVO.setFileSize(String.valueOf(file.getSize()));
        fileVO.setFileType(file.getContentType());
        fileVO.setFilePath(getPath());

        return fileVO;
    }

    @Override
    public byte[] getFileData(String fileName) {
        String rootPath = "C:/upload/";
        File file = new File(rootPath + fileName);
        try {
            if (file.exists()) {
                return Files.readAllBytes(file.toPath());
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패", e);
        }
        return null;
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

    @Override
    public void deleteAuditionApplicationFile(List<Long> fileIds) {
        fileIds.forEach(fileId -> {
            auditionApplicationFileDAO.deleteAuditionApplicationFile(fileId); // 관계 삭제
            fileDAO.deleteFile(fileId); // 파일 삭제
        });
    }

    @Override
    public List<AuditionApplicationFileDTO> findFilesByAuditionApplicationId(Long auditionApplicationId) {
        List<FileVO> fileVOList = fileDAO.findFileByAuditionApplicationId(auditionApplicationId);
        List<AuditionApplicationFileDTO> auditionApplicationFileDTOList = new ArrayList<>();

        for (FileVO fileVO : fileVOList) {
            AuditionApplicationFileDTO auditionApplicationFileDTO = new AuditionApplicationFileDTO();
            auditionApplicationFileDTO.setId(fileVO.getId());
            auditionApplicationFileDTO.setFileName(fileVO.getFileName());
            auditionApplicationFileDTO.setFilePath(fileVO.getFilePath());
            auditionApplicationFileDTO.setFileType(fileVO.getFileType());
            auditionApplicationFileDTO.setFileSize(fileVO.getFileSize());
            auditionApplicationFileDTOList.add(auditionApplicationFileDTO);
        }

        return auditionApplicationFileDTOList;
    }


    private String getPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}
