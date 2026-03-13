package com.tiendaweb.Repository;
 
import com.tiendaweb.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
 
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioId(Long usuarioId);
}