package com.app.ggumteo.mapper.file;

import com.app.ggumteo.domain.file.AuditionApplicationFileVO;
import com.app.ggumteo.domain.file.FileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuditionApplicationFileMapper {
    void insertAuditionApplicationFile(AuditionApplicationFileVO auditionApplicationFileVO);

    // 특정 postId로 게시글-파일 관계 삭제
    void deleteAuditionApplyFileById(@Param("auditionApplicationId") Long auditionApplicationId);

    // 특정 fileId로 게시글-파일 관계 삭제
    void deleteAuditionApplyFileByFileId(@Param("fileId") Long fileId);

    void insertFile(FileVO fileVO);
}
