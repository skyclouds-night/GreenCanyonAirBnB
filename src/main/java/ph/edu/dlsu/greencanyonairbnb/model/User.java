package ph.edu.dlsu.greencanyonairbnb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "app_users")
public class User {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fullname;
    private String username;
    private String role;
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Property> properties = new ArrayList<>();
}
