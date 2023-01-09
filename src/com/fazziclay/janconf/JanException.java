package com.fazziclay.janconf;

public class JanException extends RuntimeException {
    public JanException() {
    }

    public JanException(String message) {
        super(message);
    }

    public JanException(String message, Throwable cause) {
        super(message, cause);
    }

    public JanException(Throwable cause) {
        super(cause);
    }

    public JanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
