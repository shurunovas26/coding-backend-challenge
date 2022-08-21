package suggestions.algorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import suggestions.city.City;
import suggestions.api.Suggestion;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class Suggester {
    private final AutocompleteSearcher autocompleteSearcher;
    private final DistanceCalculator distanceCalculator;
    private final IdIndex idIndex;
    private final double autocompleteScoreCoefficient;
    private final double distanceScoreCoefficient;

    @Autowired
    public Suggester(AutocompleteSearcher autocompleteSearcher, DistanceCalculator distanceCalculator, IdIndex idIndex,
                     @Value("${score.coefficient.autocomplete}") double autocompleteScoreCoefficient,
                     @Value("${score.coefficient.distance}") double distanceScoreCoefficient) {
        this.autocompleteSearcher = autocompleteSearcher;
        this.distanceCalculator = distanceCalculator;
        this.idIndex = idIndex;
        this.autocompleteScoreCoefficient = autocompleteScoreCoefficient;
        this.distanceScoreCoefficient = distanceScoreCoefficient;
    }

    public List<Suggestion> search(String query, Double latitude, Double longitude) {
        List<ScoredResult> autocompleteResults = autocompleteSearcher.search(query);
        List<City> cities = autocompleteResults.stream()
                .map(ScoredResult::getId)
                .map(idIndex::getById)
                .collect(Collectors.toList());

        boolean checkDistance = latitude != null && longitude != null;

        Function<Integer, Double> scoringFunction;
        if (checkDistance) {
            List<ScoredResult> distanceResults = distanceCalculator.score(latitude, longitude, cities);
            scoringFunction = i -> calcScoreWithDistance(autocompleteResults.get(i).getScore(), distanceResults.get(i).getScore());
        } else {
            scoringFunction = i -> autocompleteResults.get(i).getScore();
        }

        return IntStream.range(0, cities.size()).mapToObj(i -> {
            City city = cities.get(i);
            return Suggestion.builder()
                    .name(String.join(",", city.getName(), city.getCountry()))
                    .latitude(city.getLatitude())
                    .longitude(city.getLongitude())
                    .score(scoringFunction.apply(i))
                    .build();
        })
                .sorted(Comparator.comparing(Suggestion::getScore).reversed())
                .collect(Collectors.toList());

    }

    private double calcScoreWithDistance(double autocompleteScore, double distanceScore) {
        return autocompleteScoreCoefficient * autocompleteScore + distanceScoreCoefficient * distanceScore;
    }


}
