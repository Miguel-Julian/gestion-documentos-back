package com.example.demo.Dao;

import com.example.demo.Model.AsignacionDocente;
import com.example.demo.Model.AsignacionPK;
import com.example.demo.Model.Curso;
import com.example.demo.Model.Docente;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IDaoAsignacionDocente extends CrudRepository <AsignacionDocente, AsignacionPK> {
    public List<AsignacionDocente> findAllByEstado(boolean state);

    public List<AsignacionDocente> findAllByDocente(Docente docente);

    //public AsignacionDocente findByCursoAndMateria (long[] idCurso);
}
