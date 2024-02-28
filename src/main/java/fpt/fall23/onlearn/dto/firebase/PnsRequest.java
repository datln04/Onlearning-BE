package fpt.fall23.onlearn.dto.firebase;

import java.util.List;

public class PnsRequest {
    private List<String> tokens;
    private String title;
    private String message;
    public PnsRequest(List<String> tokens, String title, String message) {
        super();
        this.tokens = tokens;
        this.title = title;
        this.message = message;
    }
    public List<String> getToken() {
        return tokens;
    }
    public void setToken(List<String> tokens) {
        this.tokens = tokens;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }




}