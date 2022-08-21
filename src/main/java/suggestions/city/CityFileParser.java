package suggestions.city;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CityFileParser {

    @Bean
    public List<City> cities(@Value("classpath:${cities.resource}") Resource resourceFile) throws IOException {
        //TODO: достаточно ли этого, или нужно обрабатывать какие-то случаи типа \t названии?
        // можно прикрутить парсер https://github.com/uniVocity/univocity-parsers

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resourceFile.getInputStream()))) {
            return reader.lines()
                    .skip(1)
                    .map(l -> l.split("\t"))
                    .map(arr -> City.builder()
                            .id(Long.parseLong(arr[0]))
                            .name(arr[1])
                            .country(fullCountryName(arr[8]))
                            .cc2(arr[10])
                            .latitude(Double.parseDouble(arr[4]))
                            .longitude(Double.parseDouble(arr[5])).build())
                    .collect(Collectors.toList());
        }
    }

    private String fullCountryName(String country) {
        return switch (country) {
            case "US" -> "USA";
            case "CA" -> "Canada";
            default -> country;
        };
    }
}
