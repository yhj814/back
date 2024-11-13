package com.app.ggumteo.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(String message) {
        super(message);
    }
}
