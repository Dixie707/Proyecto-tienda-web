package com.tiendaweb.service;

import com.tiendaweb.Repository.PedidoRepository;
import com.tiendaweb.Repository.ProductoRepository;
import com.tiendaweb.model.Carrito;
import com.tiendaweb.model.DetallePedido;
import com.tiendaweb.model.ItemCarrito;
import com.tiendaweb.model.Pedido;
import com.tiendaweb.model.Usuario;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public Pedido crearPedido(HttpSession session,
            Usuario usuario,
            String medioPago,
            String nombreEntrega,
            String direccion,
            String telefono,
            String metodoEntrega) {

        Carrito carrito = (Carrito) session.getAttribute("carrito");

        if (carrito == null || carrito.getItems().isEmpty() || usuario == null) {
            return null;
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setMedioPago(medioPago);
        pedido.setNombreEntrega(nombreEntrega);
        pedido.setDireccion(direccion);
        pedido.setTelefono(telefono);
        pedido.setMetodoEntrega(metodoEntrega);
        pedido.setEstado("Pendiente");

        double totalActivos = 0;

        for (ItemCarrito item : carrito.getItems()) {

            if (item.isGuardado()) {
                continue;
            }

            var productoOpt = productoRepository.findById(item.getProductoId());

            if (productoOpt.isEmpty()) {
                return null;
            }

            var producto = productoOpt.get();

            if (producto.getStock() < item.getCantidad()) {
                return null;
            }

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecio());

            // ❌ ELIMINADO: detalle.setTalla(...)

            pedido.getDetalles().add(detalle);

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            totalActivos += item.getSubtotal();
        }

        if (pedido.getDetalles().isEmpty()) {
            return null;
        }

        pedido.setTotal(totalActivos);

        Pedido guardado = pedidoRepository.save(pedido);

        carrito.getItems().removeIf(item -> !item.isGuardado());
        session.setAttribute("carrito", carrito);

        return guardado;
    }

    public boolean cancelarPedido(Long pedidoId, Long usuarioId) {

        Optional<Pedido> pedidoOpt = pedidoRepository.findById(pedidoId);

        if (pedidoOpt.isEmpty()) {
            return false;
        }

        Pedido pedido = pedidoOpt.get();

        if (pedido.getUsuario() == null || !pedido.getUsuario().getId().equals(usuarioId)) {
            return false;
        }

        if (!pedido.getEstado().equals("Pendiente")
                && !pedido.getEstado().equals("En proceso")) {
            return false;
        }

        pedido.setEstado("Cancelado");
        pedidoRepository.save(pedido);

        return true;
    }
}