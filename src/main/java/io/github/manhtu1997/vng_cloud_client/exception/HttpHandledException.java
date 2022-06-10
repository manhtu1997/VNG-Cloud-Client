package io.github.manhtu1997.vng_cloud_client.exception;

public class HttpHandledException extends RuntimeException{
    private final int code;

    public HttpHandledException(String message, int code) {
        super(message);
        this.code = code;
    }

    HttpHandledException(String message, int code, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
