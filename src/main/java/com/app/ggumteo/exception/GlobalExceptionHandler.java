package com.app.ggumteo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 세션 정보가 없는 경우 처리
    @ExceptionHandler(SessionNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleSessionNotFound(SessionNotFoundException ex) {
        log.error("Session error: {}", ex.getMessage());
        return Collections.singletonMap("error", ex.getMessage());
    }

    // 일반적인 예외 처리
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map<String, String> handleGeneralException(Exception ex) {
        log.error("Internal server error: {}", ex.getMessage());
        return Collections.singletonMap("error", "저장 중 오류가 발생했습니다.");
    }


}
