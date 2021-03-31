package com.example.demo.Security.Service;

import com.example.demo.Security.Dao.IDaoUsuario;
import com.example.demo.Security.Model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    IDaoUsuario iDaoUsuario;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void registrarUsuario (Usuario usuario){
        String a = passwordEncoder.encode(usuario.getContrasenia());
        usuario.setContrasenia(a);
        iDaoUsuario.save(usuario);
    }

    public List<Usuario> listarUsuarios () {
        return (List<Usuario>) iDaoUsuario.findAll();
    }

    public Usuario consultarUsuario(long idUsuario){
        return iDaoUsuario.findById(idUsuario).orElse(null);
    }

    public Optional<Usuario> getByNombreUsuario(String nombreUsuario){
        return iDaoUsuario.findByNombreUsuario(nombreUsuario);
    }

}
