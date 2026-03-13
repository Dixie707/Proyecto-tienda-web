package com.tiendaweb.Repository;
 
import com.tiendaweb.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
 
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();
    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
}