package ph.edu.dlsu.greencanyonairbnb.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import ph.edu.dlsu.greencanyonairbnb.security.jwt.JwtAuthEntryPoint;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebConfig {
    private final UsersDetailsService usersDetailsService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;


}
