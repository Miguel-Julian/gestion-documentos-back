package com.example.demo.Services;

import com.example.demo.Dao.IDaoCurso;
import com.example.demo.Model.Curso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoServices {
    @Autowired
    IDaoCurso iDaoCurso;

    public void registrarCurso (Curso curso) {
        iDaoCurso.save(curso);
    }

    public List<Curso> Listar(){
        return iDaoCurso.findAllByEstado(true);
    }

    public Curso consultarCurso(long idCurso){
        return iDaoCurso.findById(idCurso).orElse(null);
    }

    public Curso updateCurso (Curso curso){
        return iDaoCurso.save(curso);
    }
}
