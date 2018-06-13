package handlers;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Languages handlers
 */
@RestController
public class LanguagesHandler {
    private PatternsService service;

    /**
     * Constructor. Loads configurations
     */
    public LanguagesHandler()
    {
        service = Configs.servicesFactory.createPatternsService();
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

    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
