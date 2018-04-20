package handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import patterns.Pattern;
import patterns.UpdatePatternContent;
import services.git.CommitBasicInfo;
import services.patterns.PatternsService;
import utils.Configs;
import utils.exceptions.OldRevisionNotFound;
import utils.exceptions.PatternCreationFailedException;
import utils.exceptions.PatternNotFoundException;

import javax.servlet.http.HttpServletResponse;

/**
 * Patterns handlers
 */
@RestController
public class PatternsHandler {
    private PatternsService service;

    /**
     * Constructor. Loads configurations
     */
    public PatternsHandler()
    {
        service = Configs.servicesFactory.createPatternsService();
    }

    /**
     * Get all the patterns
     * @return List of patterns
     * @throws PatternNotFoundException When the patterns path is invalid
     */
    @RequestMapping(value = "/patterns", method = RequestMethod.GET)
    public ArrayList<Pattern> getPatterns() throws PatternNotFoundException {
        return service.getPatterns();
    }

    /**
     * Get a pattern
     * @param name Name of the pattern
     * @return The pattern requested
     * @throws PatternNotFoundException When the pattern does not exist
     */
    @RequestMapping(value = "/patterns/{name}", method = RequestMethod.GET)
    public Pattern getPattern(@PathVariable("name") String name) throws PatternNotFoundException {
        return service.getPattern(name);
    }

    /**
     * Add a new pattern
     * @param name Name of the new pattern
     * @param content Content object containing the markdown of the new pattern
     * @throws PatternCreationFailedException When the pattern already exists
     * @throws IOException When there is a problem reading the content
     */
    @RequestMapping(value = "/patterns/{name}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Pattern createPattern(@PathVariable("name") String name, @RequestBody String content)
            throws PatternCreationFailedException, IOException {

        UpdatePatternContent patternContent = new ObjectMapper().readValue(content, UpdatePatternContent.class);

        return service.createPattern(name, patternContent.getMarkdown());
    }

    /**
     * Update a requested pattern
     * @param name Name of the pattern to update
     * @param content Content object containing the markdown of the updated pattern and a message
     * @throws PatternNotFoundException When the pattern is not found
     * @throws IOException When there is a problem reading the content
     */
    @RequestMapping(value = "/patterns/{name}", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Pattern updatePattern(@PathVariable("name") String name, @RequestBody String content)
            throws PatternNotFoundException, IOException {

        UpdatePatternContent patternContent = new ObjectMapper().readValue(content, UpdatePatternContent.class);

        if(patternContent.getMessage() != null)
            service.updatePattern(name, patternContent.getMarkdown(), patternContent.getMessage());
        throw new IllegalArgumentException();
    }

    /**
     * Get pattern history
     * @param name Name of the pattern to check history for
     * @return The pattern history (commit messages and dates)
     * @throws PatternNotFoundException When the pattern does not exist
     */
    @RequestMapping(value = "/patterns/{name}/history", method = RequestMethod.GET)
    public List<CommitBasicInfo> getPatternHistory(@PathVariable("name") String name) throws PatternNotFoundException {
        return service.getPatternHistory(name);
    }

    /**
     * Get an old revision of the pattern
     * @param name The name of the pattern
     * @param sha The sha associated with the revision
     * @return The old revision of the pattern
     * @throws OldRevisionNotFound When the old revision does not exist or the pattern is not found
     */
    @RequestMapping(value = "/patterns/{name}/history/{sha}", method = RequestMethod.GET)
    public Pattern getPatternOldRevision(@PathVariable("name") String name, @PathVariable("sha") String sha) throws OldRevisionNotFound {
        return service.getPatternOldRevision(name, sha);
    }

    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
