package services.patterns;

import patterns.Pattern;
import services.git.CommitBasicInfo;
import services.git.MyGitHubService;
import utils.exceptions.OldRevisionNotFound;
import utils.exceptions.PatternCreationFailedException;
import utils.exceptions.PatternNotFoundException;

import java.io.IOException;
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
     * @throws PatternNotFoundException When the path to the patterns is invalid
     */
    public ArrayList<Pattern> getPatterns() throws PatternNotFoundException {
        ArrayList<Pattern> patterns = new ArrayList<>();

        try {
            ArrayList<String> res = gitHubService.getRepositoryContents(PATTERNS_PATH);

            for(String name: res) {
                patterns.add(new Pattern(name.replaceAll(FILE_FORMAT, "")));
            }
        } catch (IOException ex) {
            throw new PatternNotFoundException();
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
        try {
            String content = gitHubService.getFileContent(PATTERNS_PATH + name + FILE_FORMAT);

            return generatePattern(name, content);
        }
        catch (IOException ex) {
            throw new PatternNotFoundException();
        }
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

    /**
     * Update a pattern
     * @param name The name of the pattern to update
     * @param markdown The new markdown
     * @param message The update message
     * @return The updated pattern
     * @throws PatternNotFoundException When the pattern is not found
     */
    public Pattern updatePattern(String name, String markdown, String message) throws PatternNotFoundException {
        getPattern(name);
        gitHubService.commit(PATTERNS_PATH + name + FILE_FORMAT, markdown, message);
        return getPattern(name);
    }

    /**
     * Get pattern history
     * @param name The name of the pattern to check history for
     * @return The pattern history as a list of commits with message and date
     * @throws PatternNotFoundException When the pattern does not exist
     */
    public List<CommitBasicInfo> getPatternHistory(String name) throws PatternNotFoundException {
        try {
            List<CommitBasicInfo> res = gitHubService.getRepositoryCommits(PATTERNS_PATH + name + FILE_FORMAT);

            if(res.size() == 0) {
                throw new PatternNotFoundException();
            }

            return res;
        }
        catch(IOException ex) {
            throw new PatternNotFoundException();
        }
    }

    /**
     * Get pattern old revision
     * @param name The name of the pattern
     * @param sha The sha ref of the pattern
     * @return The old revision of the pattern
     * @throws OldRevisionNotFound When the pattern does not exist or the sha is invalid
     */
    public Pattern getPatternOldRevision(String name, String sha) throws OldRevisionNotFound {
        try {
            String content = gitHubService.getOldFileRevisionContent(PATTERNS_PATH + name + FILE_FORMAT, sha);

            return generatePattern(name, content);
        }
        catch (IOException ex) {
            throw new OldRevisionNotFound();
        }
    }

    /**
     * Generate a pattern
     * @param name The name of the pattern to generate
     * @param content The content of the pattern to generate
     * @return The pattern with markdown and html
     * @throws IOException When the content does not exist or no html can be obtained
     */
    public Pattern generatePattern(String name, String content) throws IOException {
        if(content != null) {
            String html = gitHubService.getHtmlFromMarkdown(content);

            if (html != null) {
                return new Pattern(name, html, content);
            } else {
                throw new IOException();
            }
        }
        else {
            throw new IOException();
        }
    }
}
