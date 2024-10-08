package com.ni.taskmaster;

public class Task {
    private String name;
    private String status;
    private String time;

    public Task(String name, String status, String time) {
        this.name = name;
        this.status = status;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() { return time; }

    public void setName(String name) { this.name = name; }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(String time) { this.time = time; }
}
