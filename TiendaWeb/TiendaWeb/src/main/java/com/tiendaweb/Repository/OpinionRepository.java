package com.tiendaweb.Repository;

import com.tiendaweb.model.Opinion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpinionRepository extends JpaRepository<Opinion, Long> {

    List<Opinion> findByProductoId(Long productoId);
}