package suggestions.algorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import suggestions.city.City;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
class IdIndex {

    private final Map<Long, City> index;

    @Autowired
    public IdIndex(List<City> cities) {
        index = cities.stream().collect(Collectors.toMap(City::getId, Function.identity()));
    }

    City getById(long id) {
        return index.get(id);
    }
}
