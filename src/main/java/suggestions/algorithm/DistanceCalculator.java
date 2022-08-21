package suggestions.algorithm;

import org.springframework.stereotype.Service;
import suggestions.city.City;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
class DistanceCalculator {

    List<ScoredResult> score(double queryLatitude, double queryLongitude, List<City> cities) {
        if (cities.isEmpty()) return Collections.emptyList();
        List<ScoredResult> scoredResults = cities.stream()
                .map(c -> ScoredResult.builder().id(c.getId())
                        .score(distance(queryLatitude, queryLongitude, c.getLatitude(), c.getLongitude())).build())
                .collect(toList());
        double maxScore = scoredResults.stream().mapToDouble(ScoredResult::getScore).max().getAsDouble();
        // чем меньше расстояние тем выше близость и нормализуем близости от 0 до 1
        scoredResults.forEach(r -> r.setScore(1 - r.getScore() / maxScore));
        return scoredResults;
    }

    // using Spherical Law of Cosines https://www.movable-type.co.uk/scripts/latlong.html
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(decimalDegreesToRadians(lat1))
                * Math.sin(decimalDegreesToRadians(lat2))
                + Math.cos(decimalDegreesToRadians(lat1))
                * Math.cos(decimalDegreesToRadians(lat2))
                * Math.cos(decimalDegreesToRadians(theta));
        dist = Math.acos(dist);
        dist = radiansToDecimalDegrees(dist);
        return dist;
    }

    private double decimalDegreesToRadians(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double radiansToDecimalDegrees(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
