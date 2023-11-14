package com.example.recipebook.servicios;

import com.example.recipebook.entidades.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class   UserService implements UserDetailsService {
    List<User> users=new ArrayList<>();

    public UserService() {
        users.add(new User("usuario1",passwordEncoder().encode("1234"),"ADMIN"));
        users.add(new User("usuario2",passwordEncoder().encode("1234"),"USER"));
        users.add(new User("usuario3",passwordEncoder().encode("1234"),"USER"));
    }
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public User findByUsername(String username) {
        return users.stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst()
                .orElse(null);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        String userRole = user.getRole();

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .roles(userRole)
                .password(user.getPassword())
                .build();
    }

}
