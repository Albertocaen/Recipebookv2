package com.example.recipebook.servicios;

import com.example.recipebook.entidades.Usuario;
import com.example.recipebook.repositorio.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository repositorio;

    private final PasswordEncoder passwordEncoder;

    public Usuario save(Usuario u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return repositorio.save(u);
    }

    public Usuario findById(long id) {
        return repositorio.findById(id).orElse(null);
    }

    public Usuario findByUsernameOrEmail(String s) {
        return repositorio.buscarPorUsernameOEmail(s).orElse(null);
    }
}
