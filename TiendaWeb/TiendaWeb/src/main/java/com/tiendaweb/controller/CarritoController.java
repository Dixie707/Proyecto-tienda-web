package com.tiendaweb.controller;
 
import com.tiendaweb.service.CarritoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
 
@Controller
@RequestMapping("/carrito")
public class CarritoController {
 
    @Autowired
    private CarritoService carritoService;
 
    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        model.addAttribute("items", carritoService.getCarrito(session));
        model.addAttribute("total", carritoService.getTotal(session));
        return "carrito/ver";
    }
 
    @PostMapping("/agregar")
    public String agregar(@RequestParam Long productoId,
                          @RequestParam(defaultValue = "1") int cantidad,
                          HttpSession session) {
        carritoService.agregar(session, productoId, cantidad);
        return "redirect:/carrito";
    }
 
    @GetMapping("/eliminar/{productoId}")
    public String eliminar(@PathVariable Long productoId, HttpSession session) {
        carritoService.eliminar(session, productoId);
        return "redirect:/carrito";
    }
}