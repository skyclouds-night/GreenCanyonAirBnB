package ph.edu.dlsu.greencanyonairbnb.response;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class JwtResponse {

    private Long ID;
    private String email;
    private String token;
    private String type = "Bearer";
    private List<String> roles;

    public JwtResponse(long ID, String email, String token, List<String> roles) {
        this.ID = ID;
        this.email = email;
        this.token = token;
        this.roles = roles;
    }
}
