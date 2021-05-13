package com.example.demo.Dao;

import com.example.demo.Model.Curso;
import com.example.demo.Model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IDaoEstudiante extends CrudRepository <Estudiante, Long> , JpaRepository<Estudiante, Long> {
    public List<Estudiante> findAllByEstado(boolean state);
    public List<Estudiante> findAllByCurso(Curso curso);
}
