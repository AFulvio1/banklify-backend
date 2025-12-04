package com.afulvio.banklifybackend.exception;

public class MissingSenderNameException extends RuntimeException {
    public MissingSenderNameException(String message) {
        super(message);
    }
}
