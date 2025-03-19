package com.example.taxinvoice.service;

//import com.example.taxinvoice.entity.User;
import com.example.taxinvoice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.User;  // Import Spring Security User

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.example.taxinvoice.entity.User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Use Spring Security's User class
        UserBuilder builder = User.withUsername(user.getEmail());
        builder.password(user.getPassword());
        builder.roles("USER"); // Assign default role

        return builder.build();
    }
}
