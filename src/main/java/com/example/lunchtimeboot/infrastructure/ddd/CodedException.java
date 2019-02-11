package com.example.lunchtimeboot.infrastructure.ddd;

public abstract class CodedException extends Exception {

    private String code;

    protected CodedException(String code, String message) {
        super(message);
        this.code = code;
    }

    protected CodedException(String code, String message, Exception cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
