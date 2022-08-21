package suggestions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import suggestions.api.SuggestionsList;
import suggestions.algorithm.Suggester;

@RestController
public class SuggestionsController {

    private final Suggester suggester;

    @Autowired
    public SuggestionsController(Suggester suggester) {
        this.suggester = suggester;
    }

    @GetMapping("/suggestions")
    public SuggestionsList getSuggestions(@RequestParam(value = "q") String query,
                                          @RequestParam(value = "latitude", required = false) Double latitude,
                                          @RequestParam(value = "longitude", required = false) Double longitude) {
        return new SuggestionsList(suggester.search(query, latitude, longitude));
    }

}
