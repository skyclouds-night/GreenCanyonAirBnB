package ph.edu.dlsu.greencanyonairbnb.service;

import ph.edu.dlsu.greencanyonairbnb.model.User;

import java.util.List;

public interface UserServiceInt {

    User registerUser(User user);
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);
}
