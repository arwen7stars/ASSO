package patterns;

import java.util.*;

/**
 * Class used to represent a Pattern
 */
public class Pattern
{
    private int id;
    private String name;
    private String intent;
    private String motivation;
    private String applicability;
    private String collaborations;
    private String consequences;
    private String implementation;
    private String sampleCode;
    private String knownUses;
    private Map<String, String> participants;
    private Map<Pattern, String> relatedPatterns;
    private String text;

    public Pattern(int id, String name)
    {
        this.id = id;
        this.name = name;
        this.participants = new HashMap<String, String>();
        this.relatedPatterns = new HashMap<Pattern, String>();
    }

    public Pattern(String name, String text)
    {
        this.name = name;
        this.text = text;
    }

    public Pattern(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }

    public String getIntent()
    {
        return intent;
    }

    public String getMotivation()
    {
        return motivation;
    }

    public String getApplicability()
    {
        return applicability;
    }

    public String getConsequences()
    {
        return consequences;
    }

    public void setCollaborations(String collaborations)
    {
        this.collaborations = collaborations;
    }

    public String getCollaborations()
    {
        return collaborations;
    }

    public void setImplementation(String implementation)
    {
        this.implementation = implementation;
    }

    public String getImplementation()
    {
        return implementation;
    }

    public void setSampleCode(String sampleCode)
    {
        this.sampleCode = sampleCode;
    }

    public String getSampleCode()
    {
        return sampleCode;
    }

    public void setKnownUses(String knownUses)
    {
        this.knownUses = knownUses;
    }

    public String getKnownUses()
    {
        return knownUses;
    }

    public Map<String, String> getParticipants()
    {
        return participants;
    }

    public void addParticipant(String name, String description)
    {
        participants.put(name, description);
    }

    public Map<Pattern, String> getRelatedPatterns()
    {
        return relatedPatterns;
    }

    public void addRelatedPattern(Pattern pattern, String message)
    {
        relatedPatterns.put(pattern, message);
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}
