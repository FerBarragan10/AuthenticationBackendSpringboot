package com.AuthenticationBakend.demo.controller;

import com.AuthenticationBakend.demo.models.AuthRequest;
import com.AuthenticationBakend.demo.models.AuthenticationResponse;

import com.AuthenticationBakend.demo.security.JwtUtil;
import com.AuthenticationBakend.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private  AuthenticationManager authenticationManager;

    @Autowired
    private  JwtUtil jwtUtil;

    @Autowired
    private UserService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        try {
            usuarioService.registerUser(request);

            // Obtén UserDetails usando el email del usuario registrado
            UserDetails rta=usuarioService.loadUserByUsername(request.getEmail());


            return ResponseEntity.ok()
                    .body("La cuenta fue creada con éxito");

        } catch (Exception e) {
            System.out.println("error");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("el email ya se encuentra registrado");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            // Autenticación del usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            UserDetails rta=usuarioService.loadUserByUsername(request.getEmail());
            String email= rta.getUsername();
            // Generación del token JWT
            String token = jwtUtil.createToken(email); // Asegúrate de que este método esté en jwtUtil

            // Respuesta exitosa con el token
            return ResponseEntity.ok(new AuthenticationResponse(token, HttpStatus.ACCEPTED.toString()));

        } catch (BadCredentialsException e) {
            // Manejo de credenciales incorrectas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales incorrectas");
        } catch (Exception e) {
            // Manejo de cualquier otra excepción
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el servidor");
        }
    }

}
