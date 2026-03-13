package com.tiendaweb.controller;
 
import com.tiendaweb.domain.Pedido;
import com.tiendaweb.domain.Usuario;
import com.tiendaweb.service.CarritoService;
import com.tiendaweb.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
 
@Controller
@RequestMapping("/pedido")
public class PedidoController {
 
    @Autowired
    private PedidoService pedidoService;
 
    @Autowired
    private CarritoService carritoService;
 
    @GetMapping("/checkout")
    public String checkoutForm(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/usuario/login";
        model.addAttribute("items", carritoService.getCarrito(session));
        model.addAttribute("total", carritoService.getTotal(session));
        return "pedido/checkout";
    }
 
    @PostMapping("/confirmar")
    public String confirmar(@RequestParam String medioPago,
                            HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/usuario/login";
 
        Pedido pedido = pedidoService.crearPedido(session, usuario, medioPago);
        return "redirect:/pedido/comprobante/" + pedido.getId();
    }
 
    @GetMapping("/comprobante/{id}")
    public String comprobante(@PathVariable Long id, Model model) {
        Optional<Pedido> op = pedidoService.buscarPorId(id);
        if (!op.isPresent()) return "redirect:/";
        model.addAttribute("pedido", op.get());
        return "pedido/comprobante";
    }
 
    @GetMapping("/misPedidos")
    public String misPedidos(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/usuario/login";
        model.addAttribute("pedidos", pedidoService.listarPorUsuario(usuario.getId()));
        return "pedido/misPedidos";
    }
}
 