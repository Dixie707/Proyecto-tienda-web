package com.tiendaweb.service;
 
import com.tiendaweb.Repository.PedidoRepository;
import com.tiendaweb.Repository.ProductoRepository;
import com.tiendaweb.model.*;
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
 
    @Autowired
    private CarritoService carritoService;
 
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
        List<ItemCarrito> items = carritoService.getCarrito(session);
 
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setMedioPago(medioPago);
        pedido.setTotal(carritoService.getTotal(session));
 
        for (ItemCarrito item : items) {
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
        carritoService.vaciar(session);
        return guardado;
    }
}