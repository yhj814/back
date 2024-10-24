package com.app.ggumteo.controller.work;


import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.service.work.WorkService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/text/*")
@RequiredArgsConstructor
@Slf4j
public class WorkController {

    private final WorkService workService;
    private final HttpSession session;

    @GetMapping("write")
    public void goToWriteForm(WorkDTO workDTO) {;}
}
