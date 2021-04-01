package com.example.demo.Controllers;

import com.example.demo.Model.Materia;
import com.example.demo.Services.MateriaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/materia")
@SessionAttributes("materiaController")

public class MateriaController {
    @Autowired
    MateriaServices materiaServices;

    @GetMapping("/listar")
    public List<Materia> listar(){
        return materiaServices.listarMaterias();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registrar")
    public List<String> registrarMateria (@Valid @RequestBody Materia materia, BindingResult bd, SessionStatus sd){
        //Verificar si hay errores
        List<String> messageList = new ArrayList<>();
        String message="";
        String[] field = {"nombreMateria", "estado"};
        if (bd.hasErrors()){
            for (String s : field) {
                if (bd.hasFieldErrors(s)) {
                    messageList.add(bd.getFieldError(s).getDefaultMessage());
                } else {
                    messageList.add(" ");
                }
            }
        } else {
            try {
                message = (materia.getIdMateria()==0)?"Se ha guardado la materia": "Datos actualizados";
                materiaServices.registrarMateria(materia);
                sd.setComplete();
            } catch (DataIntegrityViolationException e) {
                //message = getConstraintMessage(e.getMostSpecificCause().getMessage());
            } catch (Exception e) {
                message = ((materia.getIdMateria() == 0)) ? "Error al crear la materia" : "Error al realizar los cambios";
            }
            messageList.add(message);
        }
        return messageList;
    }
    @GetMapping("/consultar/{id}")
    public Materia consultarMateria (@PathVariable(value = "id") Long idMateria){
        return materiaServices.consultarMateria(idMateria);
    }
}
