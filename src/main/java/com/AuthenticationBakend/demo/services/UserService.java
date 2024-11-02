package com.AuthenticationBakend.demo.services;


import com.AuthenticationBakend.demo.models.AuthRequest;
import com.AuthenticationBakend.demo.models.Usuario;
import com.AuthenticationBakend.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Agregamos passwordEncoder al constructor
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // Usamos User.builder() de Spring Security en lugar de crear un nuevo Usuario
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(new ArrayList<>())
                .build();
    }

    // Método adicional para registrar usuarios
    public Usuario registerUser(AuthRequest data) {
        // Verificar si el usuario ya existe
        if (userRepository.findByEmail(data.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(data.getEmail());
        usuario.setPassword(passwordEncoder.encode(data.getPassword()));

        return userRepository.save(usuario);
    }


}
