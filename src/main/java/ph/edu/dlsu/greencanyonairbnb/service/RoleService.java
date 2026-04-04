package ph.edu.dlsu.greencanyonairbnb.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ph.edu.dlsu.greencanyonairbnb.exception.RoleAlreadyExistException;
import ph.edu.dlsu.greencanyonairbnb.model.Role;
import ph.edu.dlsu.greencanyonairbnb.model.User;
import ph.edu.dlsu.greencanyonairbnb.repository.RoleRepository;
import ph.edu.dlsu.greencanyonairbnb.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService implements RoleServiceInt {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role theRole) {
        String roleName = "ROLE_" + theRole.getName().toUpperCase();
        Role role = new Role(roleName);
        if (roleRepository.existsByName(roleName)){
            throw new RoleAlreadyExistException(theRole.getName()+"role already exists.");
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long roleID) {
        this.removeAllUsersFromRole(roleID);
        roleRepository.deleteById(roleID);
    }

    @Override
    public Role findByName(String guestName) {
        return roleRepository.findByName(guestName).get();
    }

    @Override
    public User removeUserFromRole(long userID, long roleID) {
        Optional<User> user = userRepository.findById(userID);
        Optional<Role> role = roleRepository.findById(roleID);
        if (role.isPresent() && role.get().getUsers().contains(user.get())){
            role.get().removeRoleFromUser(user.get());
            roleRepository.save(role.get());
            return user.get();
        }
        throw new UsernameNotFoundException("User not found");
    }

    @Override
    public User addUserToRole(long userID, long roleID) {
        Optional<User> user = userRepository.findById(userID);
        Optional<Role> role = roleRepository.findById(roleID);
        if (user.isPresent() && user.get().getRoles().contains(role.get())){
            throw new UsernameNotFoundException(user.get().getFirstName() + "is already assigned to the" + role.get().getName() + "role" );
        }
        if (role.isPresent()){
            role.get().assignRoleToUser(user.get());
            roleRepository.save(role.get());
        }
        return user.get();
    }

    @Override
    public Role removeAllUsersFromRole(long roleID) {
        Optional<Role> role = roleRepository.findById(roleID);
        role.ifPresent(Role :: removeAllUsersFromRole);
        return roleRepository.save(role.get());
    }
}
