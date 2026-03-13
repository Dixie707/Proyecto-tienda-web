package com.tiendaweb.service;

import com.tiendaweb.Repository.UsuarioRepository;
import com.tiendaweb.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo;

    public String registrar(Usuario u) {
        if(repo.findByCorreo(u.getCorreo()).isPresent()) {
            return "Correo ya registrado";
        }
        repo.save(u);
        return "Usuario registrado exitosamente";
    }

    public String login(Usuario u) {
        Optional<Usuario> usuario = repo.findByCorreo(u.getCorreo());
        if(usuario.isPresent() && usuario.get().getContraseña().equals(u.getContraseña())) {
            return "Inicio de sesión exitoso";
        }
        return "Credenciales incorrectas";
    }
}