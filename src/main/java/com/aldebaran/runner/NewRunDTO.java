package com.aldebaran.runner;

public class NewRunDTO {
    private String address, login, password, task, media;
    private Long runId;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }
}
