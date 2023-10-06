package com.example.recipebook.controladores;

import com.example.recipebook.servicios.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService service;
    @GetMapping({"/login"})
    public String login() {
        return "login";
    }
    @PostMapping({"login/submit"})
    public String loginSubmit(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        log.info("Intento de inicio de sesión para el usuario: {}", username);
        if (service.validarCredenciales(username, password)) {
            log.info("ssss");
            return "redirect:receta/list";
        } else {
            model.addAttribute("error", "Credenciales inválidas. Por favor, inténtalo de nuevo.");

            return "login";
        }
    }

}
