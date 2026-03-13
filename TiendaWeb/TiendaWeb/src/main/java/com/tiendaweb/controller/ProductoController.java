package com.tiendaweb.controller;
 
import com.tiendaweb.model.Producto;
import com.tiendaweb.service.CategoriaService;
import com.tiendaweb.service.ProductoService;
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
    private MessageSource messageSource;
 
    @GetMapping("/lista")
    public String lista(Model model) {
        model.addAttribute("productos", productoService.listar());
        model.addAttribute("categorias", categoriaService.listar());
        return "producto/lista";
    }
 
    @GetMapping("/categoria/{id}")
    public String porCategoria(@PathVariable Long id, Model model) {
        model.addAttribute("productos", productoService.listarPorCategoria(id));
        model.addAttribute("categorias", categoriaService.listar());
        return "producto/lista";
    }
 
    @GetMapping("/buscar")
    public String buscar(@RequestParam String nombre, Model model) {
        model.addAttribute("productos", productoService.buscar(nombre));
        model.addAttribute("categorias", categoriaService.listar());
        return "producto/lista";
    }
 
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Optional<Producto> op = productoService.buscarPorId(id);
        if (!op.isPresent()) return "redirect:/producto/lista";
        model.addAttribute("producto", op.get());
        return "producto/detalle";
    }
 
    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listar());
        return "producto/form";
    }
 
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Optional<Producto> op = productoService.buscarPorId(id);
        if (!op.isPresent()) return "redirect:/producto/lista";
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
                          Model model) throws Exception {
 
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
                           Locale locale) {
        productoService.eliminar(id);
        String msg = messageSource.getMessage("producto.eliminado.ok", null, locale);
        redirectAttributes.addFlashAttribute("exito", msg);
        return "redirect:/producto/lista";
    }
}