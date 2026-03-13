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

    public Pedido crearPedido(HttpSession session, Usuario usuario, String medioPago) {
        Carrito carrito = (Carrito) session.getAttribute("carrito");

        if (carrito == null || carrito.getItems().isEmpty()) {
            return null;
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setMedioPago(medioPago);
        pedido.setTotal(carrito.getTotal());

        for (ItemCarrito item : carrito.getItems()) {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecio());

            productoRepository.findById(item.getProductoId()).ifPresent(p -> {
                detalle.setProducto(p);
                p.setStock(p.getStock() - item.getCantidad());
                productoRepository.save(p);
            });

            pedido.getDetalles().add(detalle);
        }

        Pedido guardado = pedidoRepository.save(pedido);

        carrito.limpiar();
        session.setAttribute("carrito", carrito);

        return guardado;
    }
}