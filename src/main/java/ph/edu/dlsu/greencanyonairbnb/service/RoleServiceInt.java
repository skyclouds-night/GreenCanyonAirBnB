package ph.edu.dlsu.greencanyonairbnb.service;

import ph.edu.dlsu.greencanyonairbnb.model.Role;
import ph.edu.dlsu.greencanyonairbnb.model.User;

import java.util.List;

public interface RoleServiceInt {

    List<Role> getRoles();
    Role createRole (Role theRole);

    void deleteRole(Long roleID);
    Role findByName(String guestName);
    User removeUserFromRole(long userID, long roleID);
    User addUserToRole(long userID, long roleID);
    Role removeAllUsersFromRole(long roleID);
}
