package com.example.demo.Controllers;

import com.example.demo.Model.Calificacion;
import com.example.demo.Services.CalificacionServices;
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
@RequestMapping("/calificacion") //Activar servicios del curso
@SessionAttributes("calificacionController")
public class CalificacionController {

    @Autowired
    CalificacionServices calificacionServices;

    @GetMapping("/listar")
    public List<Calificacion> listar(){
        //retorna todos los registros que hay en la tabla
        return calificacionServices.Listar();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registrar")
    public List<String> registrarCalificacion (@Valid @RequestBody Calificacion calificacion, BindingResult bd, SessionStatus sd){
        //Verificar si hay errores
        List<String> messageList = new ArrayList<>();
        String message="";
        String[] field = {"nombreCalificacion", "estado"};
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
                message = (calificacion.getIdCalificacion()==0)?"Se ha guardado la calificacion": "Datos actualizados";
                calificacionServices.registrarCalificacion(calificacion);
                sd.setComplete();
            } catch (DataIntegrityViolationException e) {
                //message = getConstraintMessage(e.getMostSpecificCause().getMessage());
            } catch (Exception e) {
                message = ((calificacion.getIdCalificacion() == 0)) ? "Error al crear la calificacion" : "Error al realizar los cambios";
            }
            messageList.add(message);
        }
        return messageList;
    }

    @GetMapping("/consultar/{id}")
    public Calificacion consultarCalificacion (@PathVariable(value = "id") Long idCalificacion){
        return calificacionServices.consultarCalificacion(idCalificacion);
    }


}
