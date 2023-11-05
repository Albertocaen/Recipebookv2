package com.example.recipebook.servicios;

import com.example.recipebook.entidades.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class   UserService {
    List<User> users=new ArrayList<>();

    public UserService() {
        users.add(new User("admin","1234"));
        users.add(new User("usuario2","1234"));
        users.add(new User("usuario3","1234"));
    }
    public User findByUsername(String usuario){
        return users.stream()
                .filter(user ->usuario.equals(user.getUsername()))
                .findFirst()
                .orElse(null);
    }
    public boolean validarCredenciales(String username,String password){
        User user=findByUsername(username);
        if (user !=null && user.getPassword().equals(password)){
            return true;
        }
        return false;
    }


}
