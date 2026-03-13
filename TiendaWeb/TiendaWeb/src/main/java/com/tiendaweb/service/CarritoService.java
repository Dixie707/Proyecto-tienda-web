package com.tiendaweb.service;

import com.tiendaweb.model.Producto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarritoService {

    private List<Producto> carrito = new ArrayList<>();

    public void agregarProducto(Producto p) {
        carrito.add(p);
    }

    public List<Producto> verCarrito() {
        return carrito;
    }
}