package com.tiendaweb.model;

import java.util.ArrayList;
import java.util.List;

public class Carrito {

    private List<ItemCarrito> items = new ArrayList<>();
    private double descuento = 0;
    private String cuponAplicado;

    public List<ItemCarrito> getItems() {
        return items;
    }

    public void setItems(List<ItemCarrito> items) {
        this.items = items;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public String getCuponAplicado() {
        return cuponAplicado;
    }

    public void setCuponAplicado(String cuponAplicado) {
        this.cuponAplicado = cuponAplicado;
    }

    public void agregar(ItemCarrito item) {
        for (ItemCarrito i : items) {
            if (i.getProductoId().equals(item.getProductoId())) {
                i.setCantidad(i.getCantidad() + item.getCantidad());
                return;
            }
        }

        items.add(item);
    }

    public void aumentarCantidad(Long id) {
        for (ItemCarrito i : items) {
            if (i.getProductoId().equals(id)) {
                i.setCantidad(i.getCantidad() + 1);
                return;
            }
        }
    }

    public void disminuirCantidad(Long id) {
        ItemCarrito itemAEliminar = null;

        for (ItemCarrito i : items) {
            if (i.getProductoId().equals(id)) {
                i.setCantidad(i.getCantidad() - 1);

                if (i.getCantidad() <= 0) {
                    itemAEliminar = i;
                }

                break;
            }
        }

        if (itemAEliminar != null) {
            items.remove(itemAEliminar);
        }
    }

    public void eliminar(Long id) {
        items.removeIf(i -> i.getProductoId().equals(id));
    }

    public void limpiar() {
        items.clear();
        descuento = 0;
        cuponAplicado = null;
    }

    public double getSubtotal() {
        return items.stream()
                .mapToDouble(ItemCarrito::getSubtotal)
                .sum();
    }

    public double getMontoDescuento() {
        return getSubtotal() * descuento;
    }

    public double getTotal() {
        return getSubtotal() - getMontoDescuento();
    }

    public void guardarParaDespues(Long productoId) {
        for (ItemCarrito item : items) {
            if (item.getProductoId().equals(productoId)) {
                item.setGuardado(true);
            }
        }
    }

    public void moverAlCarrito(Long productoId) {
        for (ItemCarrito item : items) {
            if (item.getProductoId().equals(productoId)) {
                item.setGuardado(false);
            }
        }
    }

    public double getTotalActivos() {
        return items.stream()
                .filter(item -> !item.isGuardado())
                .mapToDouble(ItemCarrito::getSubtotal)
                .sum();
    }
}