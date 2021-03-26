package com.example.demo.Dao;

import com.example.demo.Model.Curso;
import com.example.demo.Model.Docente;
import com.example.demo.Model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IDaoDocente extends CrudRepository <Docente, Long> , JpaRepository<Docente, Long> {
    public List<Docente> findAllByEstado(boolean state);
}
