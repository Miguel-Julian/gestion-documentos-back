package com.example.demo.Dao;

import com.example.demo.Model.Curso;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IDaoCurso extends CrudRepository <Curso, Long> {
    public List<Curso> findAllByEstado(boolean state);
}
