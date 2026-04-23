package com.tiendaweb.controller;

import com.tiendaweb.model.Carrito;
import com.tiendaweb.model.Pedido;
import com.tiendaweb.model.Usuario;
import com.tiendaweb.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/checkout")
    public String checkoutForm(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para continuar con la compra");
            return "redirect:/usuario/login";
        }

        Carrito carrito = (Carrito) session.getAttribute("carrito");

        if (carrito == null || carrito.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Tu carrito está vacío");
            return "redirect:/carrito";
        }

        model.addAttribute("items", carrito.getItems());
        model.addAttribute("subtotal", carrito.getSubtotal());
        model.addAttribute("descuento", carrito.getMontoDescuento());
        model.addAttribute("total", carrito.getTotal());

        return "pedido/checkout";
    }

    @PostMapping("/confirmar")
    public String confirmar(@RequestParam String medioPago,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para realizar la compra");
            return "redirect:/usuario/login";
        }

        Pedido pedido = pedidoService.crearPedido(session, usuario, medioPago);

        if (pedido == null) {
            redirectAttributes.addFlashAttribute("error", "No se pudo procesar el pedido");
            return "redirect:/carrito";
        }

        return "redirect:/pedido/comprobante/" + pedido.getId();
    }

    @GetMapping("/comprobante/{id}")
    public String comprobante(@PathVariable Long id, Model model) {
        Optional<Pedido> op = pedidoService.buscarPorId(id);

        if (op.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("pedido", op.get());
        return "pedido/comprobante";
    }

    @GetMapping("/misPedidos")
    public String misPedidos(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para ver tus pedidos");
            return "redirect:/usuario/login";
        }

        model.addAttribute("pedidos", pedidoService.listarPorUsuario(usuario.getId()));
        return "pedido/misPedidos";
    }
}
