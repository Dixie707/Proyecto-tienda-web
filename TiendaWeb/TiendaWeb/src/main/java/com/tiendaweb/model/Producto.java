package com.tiendaweb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
 
@Data
@Entity
@Table(name = "productos")
public class Producto {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @NotBlank
    private String nombre;
 
    private String descripcion;
 
    @NotNull
    private Double precio;
 
    @NotNull
    private Integer stock;
 
    private String imagenUrl;
 
    private boolean activo = true;
 
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
 