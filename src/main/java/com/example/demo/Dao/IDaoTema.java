package com.example.demo.Dao;

import com.example.demo.Model.AsignacionDocente;
import com.example.demo.Model.Tema;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IDaoTema extends CrudRepository<Tema, Long> {
    public List<Tema> findAllByEstadoAndAndAsignacionDocente(boolean state,AsignacionDocente asignacionDocente);

}
