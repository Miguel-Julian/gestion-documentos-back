package com.example.demo.Services;


import com.example.demo.Dao.IDaoTema;
import com.example.demo.Model.AsignacionDocente;
import com.example.demo.Model.Tema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemaServices {
    @Autowired
    IDaoTema iDaoTema;

    public List<Tema> listarTemas (AsignacionDocente AsignacionDocente){
        return iDaoTema.findAllByEstadoAndAndAsignacionDocente(true, AsignacionDocente);
    }

    public List<Tema> listarTemasFalse (AsignacionDocente AsignacionDocente){
        return iDaoTema.findAllByEstadoAndAndAsignacionDocente(false, AsignacionDocente);
    }

    public void registrarTema (Tema tema){
        iDaoTema.save(tema);
    }

    public void borrarTema (Tema tema){
        iDaoTema.delete(tema);
    }

    public Tema consultarTema(long idTema){
        return iDaoTema.findById(idTema).orElse(null);
    }


}
