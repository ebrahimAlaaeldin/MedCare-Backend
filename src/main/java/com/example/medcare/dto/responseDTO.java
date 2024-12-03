package com.example.medcare.dto;

public class responseDTO {
    private String message;
    private boolean success;
    private int statusCode;
    private Object data;

    public responseDTO() {
    }

    public responseDTO(String message, boolean success, int statusCode, Object data) {
        this.message = message;
        this.success = success;
        this.statusCode = statusCode;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public int getStatusCode() {
        return statusCode;
    }
    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "response{" +
                "message='" + message + '\'' +
                ", success=" + success +
                ", statusCode=" + statusCode +
                ", data=" + data +
                '}';
    }

}
