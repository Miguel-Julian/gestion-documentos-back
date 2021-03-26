package com.example.demo.Dao;

import com.example.demo.Model.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface IDaoUsuario extends CrudRepository<Usuario, Long> {
}
