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
    public String checkoutForm(HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

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

        boolean tieneProductosActivos = carrito.getItems()
                .stream()
                .anyMatch(item -> !item.isGuardado());

        if (!tieneProductosActivos) {
            redirectAttributes.addFlashAttribute("error", "No tienes productos activos para comprar");
            return "redirect:/carrito";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("items", carrito.getItems());
        model.addAttribute("subtotal", carrito.getSubtotal());
        model.addAttribute("descuento", carrito.getMontoDescuento());
        model.addAttribute("total", carrito.getTotal());

        return "pedido/checkout";
    }

    @PostMapping("/confirmar")
    public String confirmar(@RequestParam String medioPago,
            @RequestParam String nombreEntrega,
            @RequestParam String direccion,
            @RequestParam String telefono,
            @RequestParam String metodoEntrega,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para realizar la compra");
            return "redirect:/usuario/login";
        }

        Pedido pedido = pedidoService.crearPedido(
                session,
                usuario,
                medioPago,
                nombreEntrega,
                direccion,
                telefono,
                metodoEntrega
        );

        if (pedido == null) {
            redirectAttributes.addFlashAttribute("error", "No se pudo procesar el pedido");
            return "redirect:/carrito";
        }

        return "redirect:/pedido/comprobante/" + pedido.getId();
    }

    @GetMapping("/comprobante/{id}")
    public String comprobante(@PathVariable Long id,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión");
            return "redirect:/usuario/login";
        }

        Optional<Pedido> op = pedidoService.buscarPorId(id);

        if (op.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Pedido no encontrado");
            return "redirect:/pedido/misPedidos";
        }

        Pedido pedido = op.get();

        if (!pedido.getUsuario().getId().equals(usuario.getId())) {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para ver este pedido");
            return "redirect:/pedido/misPedidos";
        }

        model.addAttribute("pedido", pedido);
        return "pedido/comprobante";
    }

    @GetMapping("/misPedidos")
    public String misPedidos(HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para ver tus pedidos");
            return "redirect:/usuario/login";
        }

        model.addAttribute("usuario", usuario); // 🔥 FALTABA ESTO
        model.addAttribute("pedidos", pedidoService.listarPorUsuario(usuario.getId()));

        return "usuario/perfil";
    }

    @PostMapping("/cancelar/{id}")
    public String cancelarPedido(@PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para cancelar un pedido");
            return "redirect:/usuario/login";
        }

        boolean cancelado = pedidoService.cancelarPedido(id, usuario.getId());

        if (cancelado) {
            redirectAttributes.addFlashAttribute("exito", "Pedido cancelado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo cancelar el pedido");
        }

        return "redirect:/pedido/misPedidos";
    }
}
