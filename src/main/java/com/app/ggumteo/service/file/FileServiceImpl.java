package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.repository.file.FileDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileDAO fileDAO;

    @Override
    public void register(FileVO fileVO) {fileDAO.save(fileVO);}


}
