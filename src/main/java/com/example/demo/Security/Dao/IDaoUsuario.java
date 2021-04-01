package com.example.demo.Security.Dao;

import com.example.demo.Security.Model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IDaoUsuario extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}
