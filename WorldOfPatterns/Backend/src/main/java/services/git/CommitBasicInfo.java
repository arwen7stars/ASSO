package services.git;

import java.util.Date;

public class CommitBasicInfo {
    private String message;
    private String date;

    public CommitBasicInfo(String message, String date) {
        this.message = message;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }
}
