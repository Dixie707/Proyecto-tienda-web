package com.tiendaweb.service;
 
import com.tiendaweb.Repository.CategoriaRepository;
import com.tiendaweb.model.Categoria;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
@Service
public class CategoriaService {
 
    @Autowired
    private CategoriaRepository categoriaRepository;
 
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }
 
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }
 
    public void guardar(Categoria categoria) {
        categoriaRepository.save(categoria);
    }
 
    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }
}