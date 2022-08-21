package suggestions.city;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class City {
    private long id;
    private String name;
    private String country;
    private String cc2;
    private double latitude;
    private double longitude;

}
