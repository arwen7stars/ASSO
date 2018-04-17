package services.patterns;

import patterns.Pattern;
import services.git.MyGitHubService;

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
     */
    public Pattern getPattern(String name) {
        Pattern pattern = null;

        String content = gitHubService.getFileContent(PATTERNS_PATH + name + FILE_FORMAT);

        if(content != null) {
            String html = gitHubService.getHtmlFromMarkdown(content);

            if(html != null)
                pattern = new Pattern(name, html);
        }

        return pattern;
    }

    /**
     * Create a pattern
     * @param name Pattern name
     * @param content Pattern content
     * @return True if pattern successfully created. False otherwise
     */
    public boolean createPattern(String name, String content) {
        return gitHubService.commit(PATTERNS_PATH + name + FILE_FORMAT, content, PATTERN_CREATION_MESSAGE);
    }
}
