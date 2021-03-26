package com.example.demo.Services;

import com.example.demo.Dao.IDaoMateria;
import com.example.demo.Model.Materia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MateriaServices {
    @Autowired
    IDaoMateria iDaoMateria;

    public List<Materia> listarMaterias (){
        return iDaoMateria.findAllByEstado(true);
    }

    public void registrarMateria (Materia materia){
        iDaoMateria.save(materia);
    }

    public Materia consultarMateria(long idMateria){
        return iDaoMateria.findById(idMateria).orElse(null);
    }


}
