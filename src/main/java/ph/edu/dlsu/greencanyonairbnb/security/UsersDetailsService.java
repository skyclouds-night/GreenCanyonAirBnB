package ph.edu.dlsu.greencanyonairbnb.security;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ph.edu.dlsu.greencanyonairbnb.model.User;
import ph.edu.dlsu.greencanyonairbnb.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UsersDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not Found."));
        return UserDetails.buildUserDetails(user);
    }
}
