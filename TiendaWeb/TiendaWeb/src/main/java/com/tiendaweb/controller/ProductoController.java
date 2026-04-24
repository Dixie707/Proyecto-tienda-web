package com.tiendaweb.controller;

import com.tiendaweb.model.Opinion;
import com.tiendaweb.model.Producto;
import com.tiendaweb.model.Usuario;
import com.tiendaweb.service.CategoriaService;
import com.tiendaweb.service.OpinionService;
import com.tiendaweb.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private OpinionService opinionService;

    @Autowired
    private MessageSource messageSource;

    private boolean esAdmin(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        return usuario != null && "ADMIN".equals(usuario.getRol());
    }

    @GetMapping("/lista")
    public String lista(Model model,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        if (!esAdmin(session)) {
            redirectAttributes.addFlashAttribute("error", "Acceso no autorizado");
            return "redirect:/";
        }

        model.addAttribute("productos", productoService.listar());
        model.addAttribute("categorias", categoriaService.listar());
        return "producto/lista";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {

        Optional<Producto> op = productoService.buscarPorId(id);

        if (op.isEmpty()) {
            return "redirect:/";
        }

        Producto producto = op.get();

        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.listar());
        model.addAttribute("opiniones", opinionService.listarPorProducto(producto.getId()));
        model.addAttribute("opinion", new Opinion());

        return "producto/detalle";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        if (!esAdmin(session)) {
            redirectAttributes.addFlashAttribute("error", "Acceso no autorizado");
            return "redirect:/";
        }

        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listar());
        return "producto/form";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id,
                             Model model,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        if (!esAdmin(session)) {
            redirectAttributes.addFlashAttribute("error", "Acceso no autorizado");
            return "redirect:/";
        }

        Optional<Producto> op = productoService.buscarPorId(id);

        if (op.isEmpty()) {
            return "redirect:/producto/lista";
        }

        model.addAttribute("producto", op.get());
        model.addAttribute("categorias", categoriaService.listar());
        return "producto/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") Producto producto,
                          BindingResult result,
                          @RequestParam(required = false) MultipartFile imagen,
                          RedirectAttributes redirectAttributes,
                          Locale locale,
                          Model model,
                          HttpSession session) throws Exception {

        if (!esAdmin(session)) {
            redirectAttributes.addFlashAttribute("error", "Acceso no autorizado");
            return "redirect:/";
        }

        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listar());
            return "producto/form";
        }

        productoService.guardar(producto, imagen);

        String msg = messageSource.getMessage("producto.guardado.ok", null, locale);
        redirectAttributes.addFlashAttribute("exito", msg);

        return "redirect:/producto/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
                           RedirectAttributes redirectAttributes,
                           Locale locale,
                           HttpSession session) {

        if (!esAdmin(session)) {
            redirectAttributes.addFlashAttribute("error", "Acceso no autorizado");
            return "redirect:/";
        }

        productoService.eliminar(id);

        String msg = messageSource.getMessage("producto.eliminado.ok", null, locale);
        redirectAttributes.addFlashAttribute("exito", msg);

        return "redirect:/producto/lista";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam("q") String query, Model model) {

        var productos = productoService.buscar(query);

        model.addAttribute("productos", productos);
        model.addAttribute("query", query);
        model.addAttribute("categorias", categoriaService.listar());

        return "productos";
    }

    @PostMapping("/opinar")
    public String guardarOpinion(@RequestParam Long productoId,
                                 @RequestParam int estrellas,
                                 @RequestParam String comentario,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para opinar.");
            return "redirect:/usuario/login";
        }

        Optional<Producto> productoOpt = productoService.buscarPorId(productoId);

        if (productoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado.");
            return "redirect:/";
        }

        Opinion opinion = new Opinion();
        opinion.setProducto(productoOpt.get());
        opinion.setUsuario(usuario);
        opinion.setEstrellas(estrellas);
        opinion.setComentario(comentario);

        opinionService.guardar(opinion);

        redirectAttributes.addFlashAttribute("exito", "Opinión agregada correctamente.");

        return "redirect:/producto/detalle/" + productoId;
    }
}