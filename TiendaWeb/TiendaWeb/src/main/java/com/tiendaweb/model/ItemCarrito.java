package com.tiendaweb.model;

public class ItemCarrito {

    private Long productoId;
    private String nombre;
    private double precio;
    private int cantidad;
    private String imagenUrl;
    private boolean guardado; // 🔥 IMPORTANTE

    public ItemCarrito() {
    }

    public ItemCarrito(Long productoId, String nombre, double precio, int cantidad, String imagenUrl, boolean guardado) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.imagenUrl = imagenUrl;
        this.guardado = guardado;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public double getSubtotal() {
        return precio * cantidad;
    }
}