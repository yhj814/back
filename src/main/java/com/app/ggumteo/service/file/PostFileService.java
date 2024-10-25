package com.app.ggumteo.service.file;

import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileVO;

public interface PostFileService {
    void savePostFile(PostFileVO postFileVO);
    FileVO saveFile(FileVO fileVO);
}