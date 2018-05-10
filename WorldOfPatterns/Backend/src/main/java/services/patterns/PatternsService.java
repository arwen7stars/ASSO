package services.patterns;

import patterns.Pattern;
import patterns.PatternLanguage;
import services.git.CommitBasicInfo;
import utils.exceptions.PatternCreationFailedException;
import utils.exceptions.PatternLanguageCreationFailedException;
import utils.exceptions.PatternLanguageNotFoundException;
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

    PatternLanguage createPatternLanguage(String name, ArrayList<Integer> ids) throws PatternLanguageCreationFailedException;

    PatternLanguage getPatternLanguage(int id) throws PatternLanguageNotFoundException;

    ArrayList<PatternLanguage> getPatternLanguages() throws PatternLanguageNotFoundException;

    PatternLanguage updatePatternLanguage(int id, ArrayList<Integer> ids) throws PatternLanguageNotFoundException;
}
