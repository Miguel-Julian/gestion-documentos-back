package com.example.demo.Services;

import com.example.demo.Dao.IDaoAsignacionDocente;
import com.example.demo.Model.AsignacionDocente;
import com.example.demo.Model.AsignacionPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class AsignacionDocenteServices {
    @Autowired
    IDaoAsignacionDocente iDaoAsignacionDocente;

    public void registrarAsignacion (AsignacionDocente asignacionDocente){
        iDaoAsignacionDocente.save(asignacionDocente);
    }

    public List<AsignacionDocente> listar (){
        return iDaoAsignacionDocente.findAllByEstado(true);
    }

    public AsignacionDocente consultar (AsignacionPK datos){
        //long[] datos = {1,1};
       return iDaoAsignacionDocente.findById(datos).orElse(null);
    }


}
