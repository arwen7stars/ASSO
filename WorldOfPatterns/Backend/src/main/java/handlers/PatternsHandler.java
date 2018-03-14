package handlers;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import patterns.Pattern;
import services.patterns.MySqlBasedPatternsService;
import services.patterns.PatternsService;

/**
 * Patterns handlers
 */
@RestController
public class PatternsHandler {

    PatternsService service = new MySqlBasedPatternsService();

    @RequestMapping("/patterns")
    public ArrayList<Pattern> getPatterns() {
        return service.getPatterns();
    }
}