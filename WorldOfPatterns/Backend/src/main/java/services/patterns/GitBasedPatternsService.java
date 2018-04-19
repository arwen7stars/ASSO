package services.patterns;

import patterns.Pattern;
import services.git.MyGitHubService;
import utils.exceptions.PatternCreationFailedException;
import utils.exceptions.PatternNotFoundException;

import java.util.*;

import static utils.Configs.*;

/**
 * Class that performs Patterns services using GitHub
 */
public class GitBasedPatternsService implements PatternsService{
    private static final String PATTERNS_PATH = "patterns/";
    private static final String PATTERN_CREATION_MESSAGE = "Pattern created";
    private static final String FILE_FORMAT = ".md";

    private MyGitHubService gitHubService;

    /**
     * Constructor. Sets up the GitHub service
     */
    public GitBasedPatternsService() {
        gitHubService = new MyGitHubService(GIT_USER, GIT_PASSWORD, GIT_EMAIL, GIT_REPOSITORY);
    }

    /**
     * Get all patterns
     * @return List of patterns
     */
    public ArrayList<Pattern> getPatterns() {
        ArrayList<Pattern> patterns = new ArrayList<>();

        ArrayList<String> res = gitHubService.getRepositoryContents(PATTERNS_PATH);

        for(String name: res) {
            patterns.add(new Pattern(name.replaceAll(FILE_FORMAT, "")));
        }

        return patterns;
    }

    /**
     * Get a pattern
     * @param name Name of the requested pattern
     * @return The pattern requested
     * @throws PatternNotFoundException When pattern does not exist
     */
    public Pattern getPattern(String name) throws PatternNotFoundException {
        Pattern pattern;

        String content = gitHubService.getFileContent(PATTERNS_PATH + name + FILE_FORMAT);

        if(content != null) {
            String html = gitHubService.getHtmlFromMarkdown(content);

            if(html != null)
                pattern = new Pattern(name, html, content);
            else
                throw new PatternNotFoundException();
        }
        else
            throw new PatternNotFoundException();

        return pattern;
    }

    /**
     * Create a pattern
     * @param name Pattern name
     * @param markdown Pattern markdown
     * @return The created pattern if successful. null otherwise
     * @throws PatternCreationFailedException When pattern already exists
     */
    public Pattern createPattern(String name, String markdown) throws PatternCreationFailedException {
        try {
            getPattern(name);
            throw new PatternCreationFailedException();
        }
        catch(PatternNotFoundException ex) {
            gitHubService.commit(PATTERNS_PATH + name + FILE_FORMAT, markdown, PATTERN_CREATION_MESSAGE);
        }

        try {
            return getPattern(name);
        }
        catch(PatternNotFoundException ex) {
            throw new PatternCreationFailedException();
        }
    }

    public Pattern updatePattern(String name, String markdown, String message) throws PatternNotFoundException {
        getPattern(name);
        gitHubService.commit(PATTERNS_PATH + name + FILE_FORMAT, markdown, message);
        return getPattern(name);
    }
}
