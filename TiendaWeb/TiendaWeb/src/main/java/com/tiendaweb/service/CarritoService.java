package com.tiendaweb.service;

import com.tiendaweb.model.Carrito;
import com.tiendaweb.model.ItemCarrito;
import com.tiendaweb.model.Producto;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarritoService {

    private static final String KEY = "carrito";

    @Autowired
    private ProductoService productoService;

    public Carrito getCarrito(HttpSession session) {
        Object obj = session.getAttribute(KEY);

        if (obj == null || !(obj instanceof Carrito)) {
            Carrito carrito = new Carrito();
            session.setAttribute(KEY, carrito);
            return carrito;
        }

        return (Carrito) obj;
    }

    public void agregar(HttpSession session, Long productoId, int cantidad) {
        Optional<Producto> op = productoService.buscarPorId(productoId);
        if (op.isEmpty()) return;

        Producto producto = op.get();
        Carrito carrito = getCarrito(session);

        carrito.agregar(new ItemCarrito(
                productoId,
                producto.getNombre(),
                producto.getPrecio(),
                cantidad,
                producto.getImagenUrl()
        ));

        session.setAttribute(KEY, carrito);
    }

    public void eliminar(HttpSession session, Long productoId) {
        Carrito carrito = getCarrito(session);
        carrito.eliminar(productoId);
        session.setAttribute(KEY, carrito);
    }

    public void vaciar(HttpSession session) {
        Carrito carrito = getCarrito(session);
        carrito.limpiar();
        session.setAttribute(KEY, carrito);
    }

    public Double getTotal(HttpSession session) {
        return getCarrito(session).getTotal();
    }
}