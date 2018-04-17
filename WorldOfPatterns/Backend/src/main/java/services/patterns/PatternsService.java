package services.patterns;

import patterns.Pattern;

import java.util.ArrayList;

public interface PatternsService {
    ArrayList<Pattern> getPatterns();

    Pattern getPattern(String name);

    boolean createPattern(String name, String text);
}
