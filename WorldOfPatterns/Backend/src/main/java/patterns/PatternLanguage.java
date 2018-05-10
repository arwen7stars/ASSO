package patterns;

import java.util.ArrayList;

/**
 * Class used to represent a Pattern Language
 */
public class PatternLanguage
{
    private int id;
    private String name;
    private ArrayList<Pattern> patterns = new ArrayList<>();

    public PatternLanguage(int id, String name)
    {
        this.id = id;
        this.name = name;
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

    public ArrayList<Pattern> getPatterns() {
        return patterns;
    }

    public void addPattern(Pattern pattern) {
        this.patterns.add(pattern);
    }
}
