package suggestions.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Suggestion {
    private String name;
    private double latitude;
    private double longitude;
    private double score;
}
