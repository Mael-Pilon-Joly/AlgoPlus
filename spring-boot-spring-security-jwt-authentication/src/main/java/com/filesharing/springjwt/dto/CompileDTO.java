package com.filesharing.springjwt.dto;

import java.io.InputStream;

public class CompileDTO {
    private String output;
    private String errorMessage;
    private int status;

    private  java.io.InputStream errorStream;

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public InputStream getErrorStream() {
        return errorStream;
    }

    public void setErrorStream(InputStream errorStream) {
        this.errorStream = errorStream;
    }
}
