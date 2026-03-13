package com.tiendaweb.service;

import com.tiendaweb.Repository.ProductoRepository;
import com.tiendaweb.model.Producto;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private FirebaseService firebaseService;

    public List<Producto> listar() {
        return productoRepository.findByActivoTrue();
    }

    public List<Producto> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId);
    }

    public List<Producto> buscar(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public void guardar(Producto producto, MultipartFile imagen) throws Exception {
        if (imagen != null && !imagen.isEmpty()) {
            String url = firebaseService.subirImagen(imagen);
            producto.setImagenUrl(url);
        }
        productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        Optional<Producto> op = productoRepository.findById(id);
        if (op.isPresent()) {
            Producto p = op.get();
            p.setActivo(false);
            productoRepository.save(p);
        }
        
    }
    
}