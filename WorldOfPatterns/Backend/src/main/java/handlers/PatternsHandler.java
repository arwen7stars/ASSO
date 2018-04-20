package handlers;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import patterns.Pattern;
import patterns.UpdatePatternContent;
import services.patterns.PatternsService;
import utils.Configs;
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
     */
    @RequestMapping(value = "/patterns", method = RequestMethod.GET)
    public ArrayList<Pattern> getPatterns() {
        return service.getPatterns();
    }

    /**
     * Get a pattern
     * @param name Name of the pattern
     * @return The pattern requested
     */
    @RequestMapping(value = "/patterns/{name}", method = RequestMethod.GET)
    public Pattern getPattern(@PathVariable("name") String name) throws PatternNotFoundException {
        System.out.println("asd");
        return service.getPattern(name);
    }

    /**
     * Add a new pattern
     * @param name Name of the new pattern
     * @param content Content object containing the markdown of the new pattern
     */
    @RequestMapping(value = "/patterns/{name}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Pattern createPattern(@PathVariable("name") String name, @RequestBody String content)
            throws PatternCreationFailedException, PatternNotFoundException, IOException {

        UpdatePatternContent patternContent = new ObjectMapper().readValue(content, UpdatePatternContent.class);

        return service.createPattern(name, patternContent.getMarkdown());
    }

    /**
     * Update a requested pattern
     * @param name Name of the pattern to update
     * @param content Content object containing the markdown of the updated pattern and a message
     */
    @RequestMapping(value = "/patterns/{name}", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Pattern updatePattern(@PathVariable("name") String name, @RequestBody String content)
            throws PatternCreationFailedException, PatternNotFoundException, IOException {

        UpdatePatternContent patternContent = new ObjectMapper().readValue(content, UpdatePatternContent.class);

        if(patternContent.getMessage() != null)
            service.updatePattern(name, patternContent.getMarkdown(), patternContent.getMessage());
        throw new IllegalArgumentException();
    }

    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
