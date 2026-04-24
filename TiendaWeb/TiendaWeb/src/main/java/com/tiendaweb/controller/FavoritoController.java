package com.tiendaweb.controller;

import com.tiendaweb.model.Producto;
import com.tiendaweb.model.Usuario;
import com.tiendaweb.service.FavoritoService;
import com.tiendaweb.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
@Controller
@RequestMapping("/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @Autowired
    private ProductoService productoService;

    @GetMapping("/agregar/{id}")
    public String agregarFavorito(@PathVariable Long id, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/usuario/login";
        }

        Producto producto = productoService.buscarPorId(id).orElse(null);

        if (producto == null) {
            return "redirect:/";
        }

        favoritoService.toggleFavorito(usuario, producto);

        return "redirect:/";
    }
    @GetMapping
public String verFavoritos(HttpSession session, Model model) {

    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

    if (usuario == null) {
        return "redirect:/usuario/login";
    }

    var favoritos = favoritoService.listarPorUsuario(usuario.getId());

    model.addAttribute("favoritos", favoritos);

    return "favoritos";
}
}