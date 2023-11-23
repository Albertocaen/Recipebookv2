package com.example.recipebook.seguridad;

import com.example.recipebook.servicios.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SeguridadConfig   {



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/"),
                                AntPathRequestMatcher.antMatcher("/static/**"),
                                AntPathRequestMatcher.antMatcher("/css/**"),
                                AntPathRequestMatcher.antMatcher("/img/**"),
                                AntPathRequestMatcher.antMatcher("/webjars/**"),
                                PathRequest.toH2Console()).permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());
        http.csrf(csrf ->
                csrf.ignoringRequestMatchers(
                        AntPathRequestMatcher.antMatcher("/"),
                        AntPathRequestMatcher.antMatcher("/webjars/**"),
                        AntPathRequestMatcher.antMatcher("/css/**"),
                        PathRequest.toH2Console()));
        http.headers(headers->
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));



        return http.build();
    }
    @Configuration
    public  class WebConfig implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/static/**")
                    .addResourceLocations("classpath:/static/");
        }
    }

}

