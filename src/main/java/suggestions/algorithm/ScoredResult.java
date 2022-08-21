package suggestions.algorithm;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ScoredResult {
    /**
     * id города
     */
    private long id;
    private double score;
}
