package com.tiendaweb.controller;

import com.tiendaweb.model.Categoria;
import com.tiendaweb.model.Producto;
import com.tiendaweb.service.CategoriaService;
import com.tiendaweb.service.ProductoService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/")
    public String index(Model model) {

        List<Producto> productos = productoService.listar();
        List<Categoria> categorias = categoriaService.listar();

        Map<String, List<Producto>> productosPorCategoria = new LinkedHashMap<>();

        for (Categoria categoria : categorias) {
            List<Producto> lista = productos.stream()
                    .filter(p -> p.getCategoria() != null
                    && p.getCategoria().getNombre() != null
                    && p.getCategoria().getId().equals(categoria.getId()))
                    .collect(Collectors.toList());

            productosPorCategoria.put(categoria.getNombre(), lista);
        }

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("productosPorCategoria", productosPorCategoria);
        model.addAttribute("populares", productos.stream().limit(6).toList());

        return "index";
    }
}