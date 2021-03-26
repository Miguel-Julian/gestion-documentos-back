package com.example.demo.Dao;

import com.example.demo.Model.Calificacion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IDaoCalificacion extends CrudRepository <Calificacion, Long> {
    public List<Calificacion> findAllByEstado(boolean state);
}
