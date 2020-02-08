package com.aldebaran.runner;

public class ChecksDTO {
    private byte[] checks;
    private Long environmentId;
    public ChecksDTO () {

    }

    public void setChecks(byte[] checks) {
        this.checks = checks;
    }

    public void setEnvironmentId(Long environmentId) {
        this.environmentId = environmentId;
    }
}
