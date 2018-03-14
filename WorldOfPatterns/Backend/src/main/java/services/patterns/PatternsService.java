package services.patterns;

import patterns.Pattern;

import java.util.ArrayList;

public interface PatternsService
{
    boolean createPattern(String name);

    ArrayList<Pattern> getPatterns();
}
