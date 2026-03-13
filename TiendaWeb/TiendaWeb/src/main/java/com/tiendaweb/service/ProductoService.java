package com.tiendaweb.service;

import com.tiendaweb.Repository.ProductoRepository;
import com.tiendaweb.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repo;

    public List<Producto> verCatalogo() {
        return repo.findAll();
    }
}