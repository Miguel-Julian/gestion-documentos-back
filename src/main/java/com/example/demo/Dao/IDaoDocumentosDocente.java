package com.example.demo.Dao;

import com.example.demo.Model.DocumentosDocente;
import com.example.demo.Model.Tema;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface IDaoDocumentosDocente extends CrudRepository<DocumentosDocente, Long>{
    public List<DocumentosDocente> findAllByTema(Tema tema);

}

