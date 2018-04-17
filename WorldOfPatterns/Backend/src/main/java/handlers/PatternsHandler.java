package handlers;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.*;
import patterns.Pattern;
import services.patterns.PatternsService;
import utils.Configs;

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
    @RequestMapping("/patterns")
    public ArrayList<Pattern> getPatterns() {
        return service.getPatterns();
    }

    /**
     * Get a pattern
     * @param name Name of the pattern
     * @return The pattern requested
     */
    @RequestMapping("/patterns/{name}")
    public Pattern getPattern(@PathVariable("name") String name) {
        return service.getPattern(name);
    }

    /**
     * Add a new pattern
     * @param name Name of the new pattern
     * @param content Content of the new pattern
     */
    @RequestMapping(value = "/patterns/{name}", method = RequestMethod.POST)
    public void updatePattern(@PathVariable("name") String name, @RequestParam("content") String content) {
        service.createPattern(name, content);
    }
}