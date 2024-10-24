package com.app.ggumteo.repository.file;


import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.mapper.file.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FileDAO {
    private final FileMapper fileMapper;
    //    파일 추가
    public void save(FileVO fileVO){
        fileMapper.insert(fileVO);
    }

}
