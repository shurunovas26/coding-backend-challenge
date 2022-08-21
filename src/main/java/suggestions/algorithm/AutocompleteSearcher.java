package suggestions.algorithm;

import org.apache.commons.collections4.trie.PatriciaTrie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import suggestions.city.City;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
class AutocompleteSearcher {

    private final PatriciaTrie<List<Long>> index;

    @Autowired
    public AutocompleteSearcher(List<City> cities) {
        Map<String, List<Long>> namesToIds = cities.stream()
                .collect(groupingBy(c -> c.getName().toLowerCase(),
                        mapping(City::getId, toList())));
        index = new PatriciaTrie<>(namesToIds);
    }

    /**
     * @return список результатов, отсортированных по близости к текстовому запросу
     */
    List<ScoredResult> search(String query) {
        SortedMap<String, List<Long>> trieResults = index.prefixMap(query.toLowerCase());
        return trieResults.entrySet().stream()
                .map(e -> {
                    List<Long> resultIds = e.getValue();
                    String resultName = e.getKey();
                    return resultIds.stream()
                            .map(id -> ScoredResult.builder().id(id).score(score(query, resultName)).build())
                            .collect(toList());
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private double score(String query, String result) {
        return 1 - (double) (result.length() - query.length()) / result.length();
    }

}
