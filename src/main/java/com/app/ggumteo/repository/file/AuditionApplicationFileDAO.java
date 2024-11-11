package com.app.ggumteo.repository.file;

import com.app.ggumteo.domain.file.AuditionApplicationFileVO;
import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.mapper.file.AuditionApplicationFileMapper;
import com.app.ggumteo.mapper.file.PostFileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuditionApplicationFileDAO {
    private final AuditionApplicationFileMapper auditionApplicationFileMapper;

    public void insertAuditionApplicationFile(AuditionApplicationFileVO auditionApplicationFileVO) {
        auditionApplicationFileMapper.insertAuditionApplicationFile(auditionApplicationFileVO);}

    public void deleteAuditionApplicationFile(Long auditionApplicationFileId) {auditionApplicationFileMapper.deleteAuditionApplyFileById(auditionApplicationFileId);}

    public void deleteAuditionApplyFileByFileId(Long fileId) {auditionApplicationFileMapper.deleteAuditionApplyFileByFileId(fileId);}

}
