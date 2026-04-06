package ph.edu.dlsu.greencanyonairbnb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ph.edu.dlsu.greencanyonairbnb.exception.UserAlreadyExistsException;
import ph.edu.dlsu.greencanyonairbnb.model.User;
import ph.edu.dlsu.greencanyonairbnb.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String username, String password) {

        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("Invalid username or password!");
        }
     }

    public User addUser(String fullname, String username, String password, String role) {

        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isPresent()) {
            throw new RuntimeException("username: " + username + " already exists!");
        }

        User newUser = new User();
        newUser.setFullname(fullname);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole(role);

        return userRepository.save(newUser);
     }

}
