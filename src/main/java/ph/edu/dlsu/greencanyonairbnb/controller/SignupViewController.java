package ph.edu.dlsu.greencanyonairbnb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ph.edu.dlsu.greencanyonairbnb.model.User;
import ph.edu.dlsu.greencanyonairbnb.service.UserService;

import java.util.Map;
import java.util.Optional;

@Controller

public class SignupViewController {
    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String home() {
        return "SignupView";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> addUserData(@RequestBody Map<String, String> payload) {
        try {
            User savedUser = userService.addUser(
                    payload.get("fullname"),
                    payload.get("username"),
                    payload.get("password"),
                    payload.get("role")
            );
            return ResponseEntity.ok("Success: User data added");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
