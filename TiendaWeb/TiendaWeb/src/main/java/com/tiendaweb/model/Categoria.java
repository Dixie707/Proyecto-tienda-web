package com.tiendaweb.model;
 
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
 
@Data
@Entity
@Table(name = "categorias")
public class Categoria {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @NotBlank
    private String nombre;
}
 