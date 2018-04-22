package services.patterns;

import patterns.Pattern;
import services.git.CommitBasicInfo;
import utils.exceptions.PatternCreationFailedException;
import utils.exceptions.PatternNotFoundException;

import java.util.ArrayList;
import java.util.List;

public interface PatternsService {
    ArrayList<Pattern> getPatterns();

    Pattern getPattern(int id) throws PatternNotFoundException;

    Pattern createPattern(String name, String markdown) throws PatternCreationFailedException;

    Pattern updatePattern(int id, String markdown, String message) throws PatternNotFoundException;

    List<CommitBasicInfo> getPatternHistory(int id) throws PatternNotFoundException;

    Pattern getPatternOldRevision(int id, String sha) throws PatternNotFoundException;
}
