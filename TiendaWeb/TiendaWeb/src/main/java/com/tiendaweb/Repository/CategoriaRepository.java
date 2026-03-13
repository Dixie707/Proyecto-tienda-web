package com.tiendaweb.Repository;
 
import com.tiendaweb.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
 
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}