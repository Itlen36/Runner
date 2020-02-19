package com.aldebaran.runner.dto;

import java.util.UUID;

public class ReturnCheckStatusDTO {
    private boolean passed;
    private String status;

    public ReturnCheckStatusDTO() {

    }
    public boolean isPassed() {
        return passed;
    }

    public String getStatus() {
        return status;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
