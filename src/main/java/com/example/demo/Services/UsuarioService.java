package com.example.demo.Services;

import com.example.demo.Dao.IDaoUsuario;
import com.example.demo.Model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    IDaoUsuario iDaoUsuario;

    public void registrarUsuario (Usuario usuario){
        iDaoUsuario.save(usuario);
    }

    public List<Usuario> listarUsuarios () {
        return (List<Usuario>) iDaoUsuario.findAll();
    }

    public Usuario consultarUsuario(long idUsuario){
        return iDaoUsuario.findById(idUsuario).orElse(null);
    }
}
