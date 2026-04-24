package com.tiendaweb.service;

import com.tiendaweb.Repository.FavoritoRepository;
import com.tiendaweb.model.Favorito;
import com.tiendaweb.model.Producto;
import com.tiendaweb.model.Usuario;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    public void toggleFavorito(Usuario usuario, Producto producto) {

        var existente = favoritoRepository
                .findByUsuarioIdAndProductoId(usuario.getId(), producto.getId());

        if (existente.isPresent()) {
            favoritoRepository.delete(existente.get());
        } else {
            Favorito favorito = new Favorito();
            favorito.setUsuario(usuario);
            favorito.setProducto(producto);
            favoritoRepository.save(favorito);
        }
    }

    public List<Favorito> listarPorUsuario(Long usuarioId) {
        return favoritoRepository.findByUsuarioId(usuarioId);
    }
}