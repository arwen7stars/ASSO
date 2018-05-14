package handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import patterns.Pattern;
import patterns.PatternLanguage;
import patterns.UpdatePatternContent;
import patterns.UpdatePatternLanguageContent;
import services.git.CommitBasicInfo;
import services.patterns.PatternsService;
import utils.Configs;
import utils.exceptions.*;

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
     * @param id ID of the pattern
     * @return The pattern requested
     * @throws PatternNotFoundException When the pattern does not exist
     */
    @RequestMapping(value = "/patterns/{id}", method = RequestMethod.GET)
    public Pattern getPattern(@PathVariable("id") int id) throws PatternNotFoundException {
        return service.getPattern(id);
    }

    /**
     * Add a new pattern
     * @param content Content object containing the name and the markdown of the new pattern
     * @throws PatternCreationFailedException When the pattern already exists
     * @throws IOException When there is a problem reading the content
     */
    @CrossOrigin@RequestMapping(value = "/patterns", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Pattern createPattern(@RequestBody String content)
            throws PatternCreationFailedException, IOException {

        UpdatePatternContent patternContent = new ObjectMapper().readValue(content, UpdatePatternContent.class);

        return service.createPattern(patternContent.getName(), patternContent.getMarkdown());
    }

    /**
     * Update a requested pattern
     * @param id ID of the pattern to update
     * @param content Content object containing the markdown of the updated pattern and a message
     * @throws PatternNotFoundException When the pattern is not found
     * @throws IOException When there is a problem reading the content
     */
    @CrossOrigin@RequestMapping(value = "/patterns/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Pattern updatePattern(@PathVariable("id") int id, @RequestBody String content)
            throws PatternNotFoundException, IOException {

        UpdatePatternContent patternContent = new ObjectMapper().readValue(content, UpdatePatternContent.class);

        if(patternContent.getMessage() != null)
            return service.updatePattern(id, patternContent.getMarkdown(), patternContent.getMessage());
        throw new IllegalArgumentException();
    }

    /**
     * Get pattern history
     * @param id ID of the pattern to check history for
     * @return The pattern history (commit messages and dates)
     * @throws PatternNotFoundException When the pattern does not exist
     */
    @RequestMapping(value = "/patterns/{id}/history", method = RequestMethod.GET)
    public List<CommitBasicInfo> getPatternHistory(@PathVariable("id") int id) throws PatternNotFoundException {
        return service.getPatternHistory(id);
    }

    /**
     * Get an old revision of the pattern
     * @param id The ID of the pattern
     * @param sha The sha associated with the revision
     * @return The old revision of the pattern
     * @throws OldRevisionNotFound When the old revision does not exist or the pattern is not found
     */
    @RequestMapping(value = "/patterns/{id}/history/{sha}", method = RequestMethod.GET)
    public Pattern getPatternOldRevision(@PathVariable("id") int id, @PathVariable("sha") String sha) throws OldRevisionNotFound {
        return service.getPatternOldRevision(id, sha);
    }

    /**
     * Add a new pattern language
     * @param content Content object containing the name and the patterns of the new language
     * @throws PatternLanguageCreationFailedException When the pattern language already exists
     * @throws IOException When there is a problem reading the content
     */
    @CrossOrigin@RequestMapping(value = "/languages", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public PatternLanguage createPatternLanguage(@RequestBody String content)
            throws PatternLanguageCreationFailedException, IOException {

        UpdatePatternLanguageContent patternLanguageContent = new ObjectMapper().readValue(content, UpdatePatternLanguageContent.class);

        return service.createPatternLanguage(patternLanguageContent.getName(), patternLanguageContent.getIds());
    }

    /**
     * Get all the pattern languages
     * @return List of pattern languages
     * @throws PatternLanguageNotFoundException When an error occurs
     */
    @RequestMapping(value = "/languages", method = RequestMethod.GET)
    public ArrayList<PatternLanguage> getPatternLanguages() throws PatternLanguageNotFoundException {
        return service.getPatternLanguages();
    }

    /**
     * Get a pattern language
     * @param id ID of the pattern language
     * @return The pattern language requested
     * @throws PatternLanguageNotFoundException When the pattern language does not exist
     */
    @RequestMapping(value = "/languages/{id}", method = RequestMethod.GET)
    public PatternLanguage getPatternLanguages(@PathVariable("id") int id) throws PatternLanguageNotFoundException {
        return service.getPatternLanguage(id);
    }

    /**
     * Update a requested pattern language
     * @param id ID of the pattern language to update
     * @param content Content object containing the patterns of the updated pattern language
     * @throws PatternLanguageNotFoundException When the pattern language is not found
     * @throws IOException When there is a problem reading the content
     */
    @CrossOrigin@RequestMapping(value = "/languages/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE})
    public PatternLanguage updatePatternLanguage(@PathVariable("id") int id, @RequestBody String content)
            throws PatternLanguageNotFoundException, IOException {

        UpdatePatternLanguageContent patternLanguageContent = new ObjectMapper().readValue(content, UpdatePatternLanguageContent.class);

        return service.updatePatternLanguage(id, patternLanguageContent.getIds());
    }

    /**
     * Search patterns
     * @param query The query to use in the search
     * @return All the patterns obtained in the search
     * @throws SearchFailedException When an error occurs
     */
    @RequestMapping(value = "/patterns/search/{query}", method = RequestMethod.GET)
    public List<Pattern> searchPatterns(@PathVariable("query") String query) throws SearchFailedException {
        return service.searchPatterns(query);
    }

    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
