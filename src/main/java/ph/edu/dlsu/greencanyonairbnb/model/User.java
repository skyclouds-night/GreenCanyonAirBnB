package ph.edu.dlsu.greencanyonairbnb.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,
    CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(name = "user_ID", referencedColumnName = "ID"),
    inverseJoinColumns = @JoinColumn(name = "role_ID", referencedColumnName = "ID"))
    private Collection<Role> roles = new HashSet<>();
}