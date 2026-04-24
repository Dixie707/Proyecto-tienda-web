package com.tiendaweb.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Opinion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int estrellas;
    private String comentario;

    @ManyToOne
    private Producto producto;

    @ManyToOne
    private Usuario usuario;
}