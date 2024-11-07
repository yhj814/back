package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.repository.file.FileDAO;
import com.app.ggumteo.repository.file.PostFileDAO;
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
public class PostFileServiceImpl implements PostFileService {

    private final FileDAO fileDAO;
    private final PostFileDAO postFileDAO;

    @Override
    public void savePostFile(PostFileVO postFileVO) {
        postFileDAO.insertPostFile(postFileVO);
    }

    @Override
    public FileVO saveFile(MultipartFile file, Long postId) {
        FileVO fileVO = new FileVO();
//        원본 파일 이름 저장
        fileVO.setFileName(file.getOriginalFilename());
//        파일 사이즈 값을 저장
        fileVO.setFileSize(String.valueOf(file.getSize()));
//        파일 타입을 저장
        fileVO.setFileType(file.getContentType());

//        uploads/랜덤 UUID 문자열_원본 파일 이름
        String relativePath = "uploads/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//        파일 경로 저장 <= (uploads/랜덤 UUID 문자열_원본 파일 이름)
        fileVO.setFilePath(relativePath);

//        뿌리 경로 = C:/upload/
        String rootPath = "C:/upload/";
//       파일 위치 저장 ( saveLocation <= C:/upload/ + uploads/랜덤 UUID 문자열_원본 파일 이름 )
        File saveLocation = new File(rootPath + relativePath);
        try {
//            mkdir(), mkdirs() : 디렉토리를 생성하는 기능
//            mkdir() : 만들고자 하는 디렉토리의 상위 디렉토리가 존재하지 않을 경우 생성 불가
//            mkdirs() : 만들고자 하는 디렉토리의 상위 디렉토리가 존재하지 않을 경우 상위 디렉토리까지 생성

//            만약 파일 위치에 부모 파일 존재하지 않는다면
            if (!saveLocation.getParentFile().exists()) {
//                생성하여 부모파일을 파일 위치에 저장하라.
                saveLocation.getParentFile().mkdirs();
            }
//            업로드된 파일을 지정된 경로에 저장
            file.transferTo(saveLocation);
        } catch (IOException e) {
//            예외 처리
            throw new RuntimeException("파일 저장 실패", e);
        }

//        해당 정보들을 저장하라??
        fileDAO.save(fileVO);
//        파일 DB 에서 ID 번호 가져오고, 포스트 ID 번호를....
        PostFileVO postFileVO = new PostFileVO(fileVO.getId(), postId);
//        그리고 포스트 파일 DB에 해당 정보를 넣어라.
        postFileDAO.insertPostFile(postFileVO);

//        fileVO를 리턴해라.
        return fileVO;
    }

    @Override
    public byte[] getFileData(String fileName) {
        String rootPath = "C:/upload/";
//                              C:/upload/파일 이름
        File file = new File(rootPath + fileName);
        try {
//            파일이 존재하면
            if (file.exists()) {
//                리턴해라. 파일의 모든 바이트 값을 읽는 방법으로?
                return Files.readAllBytes(file.toPath());
            }
        } catch (IOException e) {
//            예외 처리
            throw new RuntimeException("파일 읽기 실패", e);
        }
//        파일 존재하지 않는다면, null 값을?
        return null;
    }

    @Override
    public List<PostFileDTO> uploadFile(List<MultipartFile> files) throws IOException {
//        포스트파일 DTO List 객체화
        List<PostFileDTO> fileDTOs = new ArrayList<>();

//        C:/upload/경로 가져와서.
        String rootPath = "C:/upload/" + getPath();
//
        File directory = new File(rootPath);
//        ( C:/upload/경로 )가 존재하지 않는다면
        if (!directory.exists()) {
//        디렉토리를 생성하여 파일 위치에 저장하라.
            directory.mkdirs();
        }

        for (MultipartFile file : files) {
//            랜덤 uuid 를 저장하라.
            UUID uuid = UUID.randomUUID();
//            uuid 문자열_파일 원본 이름
            String uniqueFileName = uuid.toString() + "_" + file.getOriginalFilename();
//            경로/uuid 문자열_파일 원본 이름
            String relativePath = getPath() + "/" + uniqueFileName;

            PostFileDTO postFileDTO = new PostFileDTO();
//            uuid 문자열_파일 원본 이름 - 저장
            postFileDTO.setFileName(uniqueFileName);
//            경로/uuid 문자열_파일 원본 이름 - 저장
            postFileDTO.setFilePath(relativePath);
//            파일 타입 - 저장
            postFileDTO.setFileType(file.getContentType());
//            파일 사이즈 - 저장
            postFileDTO.setFileSize(String.valueOf(file.getSize()));

//           saveLocation = new File( 뿌리 경로, uuid 문자열_파일 원본 이름 )
            File saveLocation = new File(rootPath, uniqueFileName);
//            업로드된 파일을 지정된 경로에 저장??
            file.transferTo(saveLocation);

//            파일의 타입이 존재하고, 파일 타입이 image 로 시작하면
            if (file.getContentType() != null && file.getContentType().startsWith("image")) {
                //파일출력(클래스) 썸네일 =                               // 뿌리경로, t_uuid 문자열_파일 원본 이름
                try (FileOutputStream thumbnail = new FileOutputStream(new File(rootPath, "t_" + uniqueFileName))) {
                    // Thumbnailator : 이미지 썸네일을 생성할 수 있는 라이브러리
//                  // 썸네일(파일.입력 스트림, 썸네일, 100, 100) 만들어라.
                    Thumbnailator.createThumbnail(file.getInputStream(), thumbnail, 100, 100);
                }
            }
//           업로드한 파일들을 위와 같이 처리하여 -> 파일 DTO List 에 추가하라.
            fileDTOs.add(postFileDTO);
        }
//      파일 DTO List 객체를 리턴하라.
        return fileDTOs;
    }

    @Override
    public void deleteFilesByIds(List<Long> fileIds) {
        fileIds.forEach(fileId -> {
            postFileDAO.deletePostFileByFileId(fileId); // 관계 삭제
            fileDAO.deleteFile(fileId); // 파일 삭제
        });
    }

    @Override
    public List<PostFileDTO> findFilesByPostId(Long postId) {
//        파일 DTO List = postId로 파일을 찾아서 넣어라.
        List<FileVO> fileVOList = fileDAO.findFileByPostId(postId);
//        포스트 파일 DTO List 객체 생성 //
        List<PostFileDTO> postFileDTOList = new ArrayList<>();
//      파일 VO List 에 반복해서 넣어라.
        for (FileVO fileVO : fileVOList) {
//            PostFileDTO 객체 생성
            PostFileDTO postFileDTO = new PostFileDTO();
            // DB 에서 Id 가져와서
            postFileDTO.setId(fileVO.getId());
            // DB 에서 '파일이름' 가져와서
            postFileDTO.setFileName(fileVO.getFileName());
            // DB 에서 '파일경로' 가져와서
            postFileDTO.setFilePath(fileVO.getFilePath());
            // DB 에서 '파일타입' 가져와서
            postFileDTO.setFileType(fileVO.getFileType());
            // DB 에서 '파일사이즈' 가져와서
            postFileDTO.setFileSize(fileVO.getFileSize());
            // postFileDTO List 에 해당 포스트파일 DTO 객체를 추가하라.
            postFileDTOList.add(postFileDTO);
        }

//        그리고 리턴해라.
        return postFileDTOList;
    }

    private String getPath() {
//        리턴하라. 지금 지역날짜를. 해당 패턴으로 포맷해라.
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}
