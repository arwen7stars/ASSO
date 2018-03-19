package handlers;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import patterns.Pattern;
import services.patterns.MySqlBasedPatternsService;
import services.patterns.PatternsService;
import utils.Configs;

/**
 * Patterns handlers
 */
@RestController
public class PatternsHandler {

    private PatternsService service;

    public PatternsHandler()
    {
        service = Configs.databaseServicesFactory.createPatternsService();
    }

    @RequestMapping("/patterns")
    public ArrayList<Pattern> getPatterns() {
        return service.getPatterns();
    }
}