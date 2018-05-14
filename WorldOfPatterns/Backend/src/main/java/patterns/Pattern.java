package patterns;

import java.util.*;

/**
 * Class used to represent a Pattern
 */
public class Pattern
{
    private int id;
    private String name;
    private String html;
    private String markdown;

    private String lastModified;

    public Pattern(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Pattern(int id, String name, String lastModified)
    {
        this.id = id;
        this.name = name;
        this.lastModified = lastModified;
    }

    public Pattern(int id, String name, String html, String markdown)
    {
        this.id = id;
        this.name = name;
        this.html = html;
        this.markdown = markdown;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getHtml()
    {
        return html;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setHtml(String html)
    {
        this.html = html;
    }

    public String getMarkdown()
    {
        return markdown;
    }

    public void setMarkdown(String markdown)
    {
        this.markdown = markdown;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
}
