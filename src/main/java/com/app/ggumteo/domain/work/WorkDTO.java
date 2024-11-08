package com.app.ggumteo.domain.work;

import com.app.ggumteo.domain.post.PostVO;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class WorkDTO {
    private Long id; // 작품 ID
    private String workPrice; // 작품 가격
    private String genreType; // 장르 타입
    private int readCount; // 조회수
    private Long postId;
    private Long memberProfileId;
    private String profileName;
    private String profileNickName;
    private String profileEmail;
    private String postTitle;
    private String postContent;
    private String postType;
    private String fileContent;
    private double star;
    private String createdDate; // 생성 날짜
    private String updatedDate; // 수정 날짜
    private String thumbnailFileName; // 썸네일 파일 경로
    private String thumbnailFilePath; // 썸네일 파일 경로(수정 전)
    private Long thumbnailFileId; // 썸네일 파일 ID (추가된 필드)
    private List<Long> ids; // 삭제할 파일 id 목록
    private String profileImgUrl; // 카카오톡 프로필 이미지 url
    private List<Long> fileIds;
    private List<MultipartFile> files;
    private MultipartFile workFile;
    private MultipartFile thumbnailFile;

    public WorkVO toVO() {
        return new WorkVO(id, workPrice, genreType, fileContent, readCount, createdDate, updatedDate, thumbnailFileId);
    }

    public PostVO toPostVO() {
        return new PostVO(id, postTitle, postContent, postType, memberProfileId, createdDate, updatedDate);
    }
}
