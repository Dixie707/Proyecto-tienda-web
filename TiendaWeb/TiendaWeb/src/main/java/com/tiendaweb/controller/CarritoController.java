package com.tiendaweb.controller;

import com.tiendaweb.model.Carrito;
import com.tiendaweb.model.ItemCarrito;
import com.tiendaweb.model.Producto;
import com.tiendaweb.service.CategoriaService;
import com.tiendaweb.service.ProductoService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("carrito")
public class CarritoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @ModelAttribute("carrito")
    public Carrito carrito() {
        return new Carrito();
    }

    @GetMapping("/carrito")
    public String verCarrito(@ModelAttribute("carrito") Carrito carrito, Model model) {
        model.addAttribute("items", carrito.getItems());
        model.addAttribute("total", carrito.getTotal());
        model.addAttribute("categorias", categoriaService.listar());
        return "carrito";
    }

    // ✅ AGREGAR SIN TALLA
    @PostMapping("/carrito/agregar/{id}")
    public String agregarProducto(@PathVariable Long id,
                                 @ModelAttribute("carrito") Carrito carrito,
                                 RedirectAttributes redirectAttributes) {

        Optional<Producto> productoOpt = productoService.buscarPorId(id);

        if (productoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado.");
            return "redirect:/";
        }

        Producto producto = productoOpt.get();

        if (producto.getStock() <= 0) {
            redirectAttributes.addFlashAttribute("error", "Este producto está fuera de stock.");
            return "redirect:/";
        }

        int cantidadActual = carrito.getItems().stream()
                .filter(item -> item.getProductoId().equals(id))
                .filter(item -> !item.isGuardado())
                .mapToInt(ItemCarrito::getCantidad)
                .sum();

        if (cantidadActual >= producto.getStock()) {
            redirectAttributes.addFlashAttribute("error",
                    "No puedes agregar más unidades. Stock disponible: " + producto.getStock());
            return "redirect:/";
        }

        ItemCarrito item = new ItemCarrito(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                1,
                producto.getImagenUrl(),
                false // guardado
        );

        carrito.agregar(item);

        redirectAttributes.addFlashAttribute("exito", "Producto agregado al carrito.");
        return "redirect:/";
    }

    // ✅ AUMENTAR SIN TALLA
    @GetMapping("/carrito/aumentar/{id}")
    public String aumentarCantidad(@PathVariable Long id,
                                  @ModelAttribute("carrito") Carrito carrito,
                                  RedirectAttributes redirectAttributes) {

        Optional<Producto> productoOpt = productoService.buscarPorId(id);

        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();

            int cantidadActual = carrito.getItems().stream()
                    .filter(item -> item.getProductoId().equals(id))
                    .filter(item -> !item.isGuardado())
                    .mapToInt(ItemCarrito::getCantidad)
                    .sum();

            if (cantidadActual >= producto.getStock()) {
                redirectAttributes.addFlashAttribute("error",
                        "No hay más stock disponible para este producto.");
                return "redirect:/carrito";
            }
        }

        carrito.aumentarCantidad(id);
        return "redirect:/carrito";
    }

    // ✅ DISMINUIR SIN TALLA
    @GetMapping("/carrito/disminuir/{id}")
    public String disminuirCantidad(@PathVariable Long id,
                                    @ModelAttribute("carrito") Carrito carrito) {

        carrito.disminuirCantidad(id);
        return "redirect:/carrito";
    }

    // ✅ ELIMINAR SIN TALLA
    @GetMapping("/carrito/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id,
                                   @ModelAttribute("carrito") Carrito carrito) {

        carrito.eliminar(id);
        return "redirect:/carrito";
    }

    @GetMapping("/carrito/limpiar")
    public String limpiarCarrito(@ModelAttribute("carrito") Carrito carrito) {
        carrito.limpiar();
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/aplicar-cupon")
    public String aplicarCupon(@RequestParam("cupon") String cupon,
                               @ModelAttribute("carrito") Carrito carrito,
                               RedirectAttributes redirectAttributes) {

        if (cupon == null || cupon.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Debes ingresar un cupón.");
            return "redirect:/carrito";
        }

        cupon = cupon.trim().toUpperCase();

        if ("DESCUENTO10".equals(cupon)) {
            carrito.setDescuento(0.10);
            carrito.setCuponAplicado(cupon);
            redirectAttributes.addFlashAttribute("exito",
                    "Cupón aplicado correctamente: 10% de descuento.");
        } else {
            carrito.setDescuento(0);
            carrito.setCuponAplicado(null);
            redirectAttributes.addFlashAttribute("error",
                    "El código de descuento no es válido.");
        }

        return "redirect:/carrito";
    }

    // ✅ GUARDAR SIN TALLA
    @GetMapping("/carrito/guardar/{id}")
    public String guardarParaDespues(@PathVariable Long id,
                                     @ModelAttribute("carrito") Carrito carrito) {

        carrito.guardarParaDespues(id);
        return "redirect:/carrito";
    }

    // ✅ MOVER SIN TALLA
    @GetMapping("/carrito/mover/{id}")
    public String moverAlCarrito(@PathVariable Long id,
                                 @ModelAttribute("carrito") Carrito carrito) {

        carrito.moverAlCarrito(id);
        return "redirect:/carrito";
    }
}