package com.tiendaweb.service;

import com.tiendaweb.model.DatosPago;
import com.tiendaweb.model.Pedido;
import com.tiendaweb.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepo;

    public String registrarPedido(DatosPago pago) {
        if(!pago.esValido()) {
            return "Pago rechazado";
        }

        // Crear un nuevo pedido
        Pedido pedido = new Pedido();
        pedido.setEstado("Completado");
        pedido.setMonto(100.0); 
        pedidoRepo.save(pedido);

        return "Pedido registrado con pago válido";
    }
}