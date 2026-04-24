package com.tiendaweb.controller;

import com.tiendaweb.model.Usuario;
import com.tiendaweb.service.UsuarioService;
import com.tiendaweb.service.PedidoService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private MessageSource messageSource;

    // =========================
    // LOGIN
    // =========================
    @GetMapping("/login")
    public String loginForm() {
        return "usuario/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String correo,
                        @RequestParam String contrasena,
                        HttpSession session,
                        RedirectAttributes redirectAttributes,
                        Locale locale) {

        Usuario usuario = usuarioService.login(correo, contrasena);

        if (usuario != null) {
            session.setAttribute("usuarioLogueado", usuario);
            return "redirect:/";
        }

        String msg = messageSource.getMessage("usuario.login.error", null, locale);
        redirectAttributes.addFlashAttribute("error", msg);
        return "redirect:/usuario/login";
    }

    // =========================
    // REGISTRO
    // =========================
    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/registro";
    }

    @PostMapping("/registro")
    public String registro(@Valid @ModelAttribute("usuario") Usuario usuario,
                           BindingResult result,
                           @RequestParam("confirmarContrasena") String confirmarContrasena,
                           RedirectAttributes redirectAttributes,
                           Model model,
                           Locale locale) {

        if (result.hasErrors()) {
            return "usuario/registro";
        }

        if (!usuario.getContrasena().equals(confirmarContrasena)) {
            model.addAttribute("errorGeneral", "Las contraseñas no coinciden");
            return "usuario/registro";
        }

        if (usuarioService.existeCorreo(usuario.getCorreo())) {
            String msg = messageSource.getMessage("usuario.correo.duplicado", null, locale);
            result.rejectValue("correo", "error.usuario", msg);
            return "usuario/registro";
        }

        usuario.setRol("USER");
        usuarioService.guardar(usuario);

        String msg = messageSource.getMessage("usuario.registro.ok", null, locale);
        redirectAttributes.addFlashAttribute("exito", msg);
        return "redirect:/usuario/login";
    }

    // =========================
    // LOGOUT
    // =========================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/usuario/login";
    }

    // =========================
    // PERFIL + HISTORIAL (HU-10 + HU-16)
    // =========================
    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/usuario/login";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("pedidos", pedidoService.listarPorUsuario(usuario.getId()));

        return "usuario/perfil";
    }
    @GetMapping("/recuperar")
public String recuperarForm() {
    return "usuario/recuperar";
}

@PostMapping("/recuperar")
public String recuperar(@RequestParam String correo,
                        @RequestParam String nuevaContrasena,
                        RedirectAttributes redirectAttributes) {

    boolean actualizado = usuarioService.actualizarContrasena(correo, nuevaContrasena);

    if (actualizado) {
        redirectAttributes.addFlashAttribute("exito", "Contraseña actualizada correctamente");
        return "redirect:/usuario/login";
    }

    redirectAttributes.addFlashAttribute("error", "El correo no existe");
    return "redirect:/usuario/recuperar";
}
@PostMapping("/pedido/cancelar/{id}")
public String cancelarPedido(@PathVariable Long id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

    if (usuario == null) {
        return "redirect:/usuario/login";
    }

    boolean cancelado = pedidoService.cancelarPedido(id, usuario.getId());

    if (cancelado) {
        redirectAttributes.addFlashAttribute("exito", "Pedido cancelado correctamente");
    } else {
        redirectAttributes.addFlashAttribute("error", "No se pudo cancelar el pedido");
    }

    return "redirect:/usuario/perfil";
}
}