package com.AuthenticationBakend.demo.models;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")  // Cambiado para coincidir con la BD
    private Long id;

    @Column(name = "email", unique = true)  // Cambiado para coincidir con la BD
    private String email;

    @Column(name = "password")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    // Si estás guardando nuevos usuarios, asegúrate de encriptar la contraseña:
    public void setPassword(String password) {
        // Solo encriptar si no está ya encriptada
        if (!password.startsWith("$2a$")) {
            this.password = new BCryptPasswordEncoder().encode(password);
        } else {
            this.password = password;
        }
    }

}
