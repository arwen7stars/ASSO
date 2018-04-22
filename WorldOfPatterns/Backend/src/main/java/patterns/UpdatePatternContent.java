package patterns;

public class UpdatePatternContent {
    private String markdown;
    private String message;
    private String name;

    public String getMarkdown() {
        return markdown;
    };

    public String getMessage() {
        return message;
    }

    public String getName() { return name; }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    };

    public void setMessage(String message) {
        this.message = message;
    }

    public void setName(String name) { this.name = name; }
}
