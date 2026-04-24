package com.tiendaweb.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha = new Date();

    private Double total;

    private String medioPago;

    private String estado = "PAGADO";

    // 🔽 HU-12 DIRECCIÓN DE ENVÍO
    private String nombreEntrega;
    private String direccion;
    private String telefono;
    private String metodoEntrega; // ENVIO o RETIRO

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedido> detalles = new ArrayList<>();
}