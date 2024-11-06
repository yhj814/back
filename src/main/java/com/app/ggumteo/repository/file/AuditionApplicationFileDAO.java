package com.app.ggumteo.repository.file;

import com.app.ggumteo.domain.file.AuditionApplicationFileVO;
import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.mapper.file.AuditionApplicationFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AuditionApplicationFileDAO {

    @Autowired
    private AuditionApplicationFileMapper auditionApplicationFileMapper;

    public void insertAuditionApplicationFile(AuditionApplicationFileVO auditionApplicationFileVO) {
        auditionApplicationFileMapper.insertAuditionApplicationFile(auditionApplicationFileVO);}

    public void insertFile(FileVO fileVO) {
        auditionApplicationFileMapper.insertFile(fileVO);
    }

}
