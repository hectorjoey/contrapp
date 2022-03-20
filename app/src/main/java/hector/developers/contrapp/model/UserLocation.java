package hector.developers.contrapp.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLocation implements Serializable {

    private Long id;
    private String latitude;
    private String longitude;
    private String address;
    private String date;
    private Long userId;
}
