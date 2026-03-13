package com.tiendaweb.model;
 
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
 
@Data
@Entity
@Table(name = "usuarios")
public class Usuario {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @NotBlank
    private String nombre;
 
    @NotBlank
    @Email
    @Column(unique = true)
    private String correo;
 
    @NotBlank
    private String contrasena;
 
    private String rol = "USER";
}
 