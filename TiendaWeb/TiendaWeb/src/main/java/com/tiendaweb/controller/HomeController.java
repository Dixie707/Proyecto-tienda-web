package com.tiendaweb.controller;
 
import com.tiendaweb.service.CategoriaService;
import com.tiendaweb.service.ProductoService;
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
        model.addAttribute("productos", productoService.listar());
        model.addAttribute("categorias", categoriaService.listar());
        return "index";
    }
}