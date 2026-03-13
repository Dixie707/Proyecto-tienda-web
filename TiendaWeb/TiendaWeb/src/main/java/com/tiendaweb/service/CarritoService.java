package com.tiendaweb.service;
 
import com.tiendaweb.model.ItemCarrito;
import com.tiendaweb.model.Producto;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
@Service
public class CarritoService {
 
    private static final String KEY = "carrito";
 
    @Autowired
    private ProductoService productoService;
 
    @SuppressWarnings("unchecked")
    public List<ItemCarrito> getCarrito(HttpSession session) {
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute(KEY);
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute(KEY, carrito);
        }
        return carrito;
    }
 
    public void agregar(HttpSession session, Long productoId, int cantidad) {
        Optional<Producto> op = productoService.buscarPorId(productoId);
        if (!op.isPresent()) return;
        Producto producto = op.get();
 
        List<ItemCarrito> carrito = getCarrito(session);
        for (ItemCarrito item : carrito) {
            if (item.getProductoId().equals(productoId)) {
                item.setCantidad(item.getCantidad() + cantidad);
                session.setAttribute(KEY, carrito);
                return;
            }
        }
        carrito.add(new ItemCarrito(productoId, producto.getNombre(),
            producto.getPrecio(), cantidad, producto.getImagenUrl()));
        session.setAttribute(KEY, carrito);
    }
 
    public void eliminar(HttpSession session, Long productoId) {
        List<ItemCarrito> carrito = getCarrito(session);
        carrito.removeIf(i -> i.getProductoId().equals(productoId));
        session.setAttribute(KEY, carrito);
    }
 
    public void vaciar(HttpSession session) {
        session.removeAttribute(KEY);
    }
 
    public Double getTotal(HttpSession session) {
        double total = 0;
        for (ItemCarrito item : getCarrito(session)) {
            total += item.getSubtotal();
        }
        return total;
    }
}