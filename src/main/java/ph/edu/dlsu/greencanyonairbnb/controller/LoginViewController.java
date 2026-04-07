package ph.edu.dlsu.greencanyonairbnb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ph.edu.dlsu.greencanyonairbnb.model.User;
import ph.edu.dlsu.greencanyonairbnb.service.UserService;

import java.util.Map;
import java.util.Optional;

@Controller
public class LoginViewController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String home() {
        return "LoginView";
    }

    @PostMapping("/login")
    public ResponseEntity<String> processUserData(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        try {
            User savedUser = userService.getUser(
                    payload.get("username"),
                    payload.get("password")
            );
            return ResponseEntity.ok("Success");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}