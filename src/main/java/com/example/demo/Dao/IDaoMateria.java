package com.example.demo.Dao;

import com.example.demo.Model.Materia;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IDaoMateria extends CrudRepository <Materia, Long> {
    public List<Materia> findAllByEstado(boolean state);
}
