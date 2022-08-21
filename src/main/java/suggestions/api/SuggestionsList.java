package suggestions.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SuggestionsList {
    List<Suggestion> suggestions;
}
