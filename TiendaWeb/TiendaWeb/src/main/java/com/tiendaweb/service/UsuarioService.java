package com.tiendaweb.service;
 
import com.tiendaweb.Repository.UsuarioRepository;
import com.tiendaweb.model.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
@Service
public class UsuarioService {
 
    @Autowired
    private UsuarioRepository usuarioRepository;
 
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }
 
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
 
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
 
    public void guardar(Usuario usuario) {
        usuarioRepository.save(usuario);
    }
 
    public boolean existeCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }
 
    public Optional<Usuario> login(String correo, String contrasena) {
        return usuarioRepository.findByCorreo(correo)
            .filter(u -> u.getContrasena().equals(contrasena));
    }
}