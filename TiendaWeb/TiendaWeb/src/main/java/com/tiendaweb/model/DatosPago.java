package com.tiendaweb.model;
 
import jakarta.persistence.*;
import lombok.Data;
 
@Data
@Entity
@Table(name = "datos_pago")
public class DatosPago {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @OneToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
 
    // Tarjeta, SINPE, Efectivo
    private String medioPago;
 
    private Double monto;
 
    private String estado = "APROBADO";
 
    // Para SINPE: número de confirmación
    private String referencia;
 
    // Últimos 4 dígitos si es tarjeta
    private String ultimosDigitos;
}
 