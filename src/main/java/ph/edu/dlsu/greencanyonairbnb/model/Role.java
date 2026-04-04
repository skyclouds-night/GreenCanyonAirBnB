package ph.edu.dlsu.greencanyonairbnb.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.apache.catalina.realm.UserDatabaseRealm.getRoles;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String guestName;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users = new HashSet<>();

    public Role(String guestName) {
        this.guestName = guestName;
    }

    public void assignRoleToUser(User user){
        user.getRoles().add(this);
        this.getUsers().add(user);
    }

    public void removeRoleFromUser(User user){
        user.getRoles().remove(this);
        this.getUsers().remove(user);
    }

    public void removeAllUsersFromRole(){
        if(this.getUsers() != null){
            List<User> roleUsers = this.getUsers().stream().toList();
            roleUsers.forEach(this :: removeRoleFromUser);
        }
    }

    public String getName(){
        return guestName != null? guestName : "";
    }

}
