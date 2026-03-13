package com.tiendaweb.Repository;

import com.tiendaweb.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrue();

    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);

    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

    // Para HU-08 detalle de producto
    Optional<Producto> findByIdAndActivoTrue(Long id);

    // Para HU-18 productos populares
    List<Producto> findTop6ByActivoTrueOrderByIdDesc();
}