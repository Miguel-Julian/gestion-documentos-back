package com.example.demo.Dao;

import com.example.demo.Model.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IDaoDocumentosEstudiante extends CrudRepository <DocumentosEstudiante, DocumentosEstudiantePK> {
    public List<DocumentosEstudiante> findAllByEstado(boolean state);
    public List<DocumentosEstudiante> findAllByDocumentosDocente(DocumentosDocente documentosDocente);
}
