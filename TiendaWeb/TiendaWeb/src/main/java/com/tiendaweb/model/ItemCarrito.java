package com.tiendaweb.model;
 
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarrito implements Serializable {
 
    private Long productoId;
    private String nombre;
    private Double precio;
    private Integer cantidad;
    private String imagenUrl;
 
    public Double getSubtotal() {
        return precio * cantidad;
    }
}
