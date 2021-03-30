package com.example.demo.Services;

import com.example.demo.Dao.IDaoCalificacion;
import com.example.demo.Model.Calificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalificacionServices {
    @Autowired
    IDaoCalificacion iDaoCalificacion;

    public void registrarCalificacion (Calificacion calificacion) {
        iDaoCalificacion.save(calificacion);
    }

    public List<Calificacion> Listar(){
        return iDaoCalificacion.findAllByEstado(true);
    }

    public Calificacion consultarCalificacion(long idCalificacion){
        return iDaoCalificacion.findById(idCalificacion).orElse(null);
    }
}
