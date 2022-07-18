package com.filesharing.springjwt.dto;

public class ExerciseDTO {
    private Long id;
    private String clientId;
    private String clientSecret;
    private String script;
    private String language;
    private String versionIndex;

    private String stdin;

    public ExerciseDTO() {
    }

    public ExerciseDTO(String clientId, String clientSecret, String script, String language, String versionIndex) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.script = script;
        this.language = language;
        this.versionIndex = versionIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStdin() {
        return stdin;
    }

    public void setStdin(String stdin) {
        this.stdin = stdin;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVersionIndex() {
        return versionIndex;
    }

    public void setVersionIndex(String versionIndex) {
        this.versionIndex = versionIndex;
    }

    public String getStdIn() {
        return stdin;
    }

    public void setStdIn(String stdIn) {
        this.stdin = stdIn;
    }
}
