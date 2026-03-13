package com.tiendaweb.model;
 
import jakarta.persistence.*;
import lombok.Data;
 
@Data
@Entity
@Table(name = "detalle_pedidos")
public class DetallePedido {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
 
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
 
    private Integer cantidad;
 
    private Double precioUnitario;
 
    public Double getSubtotal() {
        return cantidad * precioUnitario;
    }
}