package com.leafresh.backend.oauth.payload;

public class ApiResponse {
    private boolean success;
    private String message;

    // 생성자
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getter 메서드
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    // Setter 메서드
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
