package services.patterns;

import patterns.Pattern;
import utils.exceptions.PatternCreationFailedException;
import utils.exceptions.PatternNotFoundException;

import java.util.ArrayList;

public interface PatternsService {
    ArrayList<Pattern> getPatterns();

    Pattern getPattern(String name) throws PatternNotFoundException;

    Pattern createPattern(String name, String markdown) throws PatternCreationFailedException;

    Pattern updatePattern(String name, String markdown, String message) throws PatternNotFoundException;
}
