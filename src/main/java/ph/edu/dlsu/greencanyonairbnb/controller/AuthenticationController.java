package ph.edu.dlsu.greencanyonairbnb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ph.edu.dlsu.greencanyonairbnb.security.jwt.JwtUtils;
import ph.edu.dlsu.greencanyonairbnb.service.UserServiceInt;


@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserServiceInt userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
}
