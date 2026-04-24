package com.tiendaweb.service;

import com.tiendaweb.Repository.OpinionRepository;
import com.tiendaweb.model.Opinion;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpinionService {

    @Autowired
    private OpinionRepository opinionRepository;

    public List<Opinion> listarPorProducto(Long productoId) {
        return opinionRepository.findByProductoId(productoId);
    }

    public void guardar(Opinion opinion) {
        opinionRepository.save(opinion);
    }
}