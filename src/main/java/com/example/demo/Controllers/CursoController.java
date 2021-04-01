package com.example.demo.Controllers;

import com.example.demo.Model.Curso;
import com.example.demo.Services.CursoServices;
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
@CrossOrigin(origins = "*") //Recibe peticiones
@RequestMapping("/curso") //Activar servicios del curso
@SessionAttributes("cursoController")

public class CursoController {

    @Autowired
    CursoServices cursoServices;


    @GetMapping("/listar")
    public List<Curso> listar(){
        //retorna todos los registros que hay en la tabla
        return cursoServices.Listar();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registrar")
    public List<String> registrarCurso (@Valid @RequestBody Curso curso, BindingResult bd, SessionStatus sd){
        //Verificar si hay errores
        List<String> messageList = new ArrayList<>();
        String message="";
        String[] field = {"nombreCurso", "estado"};
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
                message = (curso.getIdCurso()==0)?"Se ha guardado el curso": "Datos actualizados";
                cursoServices.registrarCurso(curso);
                sd.setComplete();
            } catch (DataIntegrityViolationException e) {
                //message = getConstraintMessage(e.getMostSpecificCause().getMessage());
            } catch (Exception e) {
                message = ((curso.getIdCurso() == 0)) ? "Error al crear el curso" : "Error al realizar los cambios";
            }
            messageList.add(message);
        }
        return messageList;
    }

    @GetMapping("/consultar/{id}")
    public Curso consultarCurso (@PathVariable(value = "id") Long idCurso){
        return cursoServices.consultarCurso(idCurso);
    }


}
