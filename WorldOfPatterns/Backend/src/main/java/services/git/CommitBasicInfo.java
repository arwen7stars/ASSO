package services.git;

import java.util.Date;

public class CommitBasicInfo {
    private String message;
    private String date;
    private String sha;

    public CommitBasicInfo(String message, String date, String sha) {
        this.message = message;
        this.date = date;
        this.sha = sha;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getSha() {
        return sha;
    }
}
