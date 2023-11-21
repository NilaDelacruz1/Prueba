package com.example.demo.CapaSeguridad.exception;

public class ErrorMessage {
    Integer statusCode;
    String Reason;

    public ErrorMessage(Integer statusCode, String reason) {
        this.statusCode = statusCode;
        Reason = reason;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }
}
