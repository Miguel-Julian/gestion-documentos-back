package com.example.demo.Controllers;

import com.example.demo.Model.AsignacionDocente;
import com.example.demo.Model.AsignacionPK;
import com.example.demo.Model.TipoDocumento;
import com.example.demo.Services.AsignacionDocenteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/asignacion")
@SessionAttributes("asignacionDocenteController")

public class AsignacionDocenteController {
    @Autowired
    AsignacionDocenteServices asignacionDocenteServices;

    @GetMapping("/listar")
    public List<AsignacionDocente> listar(){
        //retorna todos los registros que hay en la tabla
        return asignacionDocenteServices.listar();
    }

    @PostMapping("/registrar")
    public List<String> registrarAsignacion (@Valid @RequestBody AsignacionDocente asignacionDocente, BindingResult bd, SessionStatus sd){
        //verificar si se esta guardando o actualizando
        List<AsignacionDocente> asignacionDocentes = asignacionDocenteServices.listar();
        boolean flag = true;
        for(int i = 0; i < asignacionDocentes.size(); i++) {
            if (asignacionDocentes.get(i).getCurso().getIdCurso()==asignacionDocente.getCurso().getIdCurso() && asignacionDocentes.get(i).getMateria().getIdMateria()==asignacionDocente.getMateria().getIdMateria()){
                flag = false;
            }
        }
        //Verificar si hay errores
        List<String> messageList = new ArrayList<>();
        String message="";
        String[] field = {"nombreAsignacion", "estado"};
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

                message = (flag)?"Se ha asignado correctamente el curso y la materia": "Datos actualizados";
                AsignacionPK asignacionPK = new AsignacionPK();
                asignacionPK.setIdCurso(asignacionDocente.getCurso().getIdCurso());
                asignacionPK.setIdMateria(asignacionDocente.getMateria().getIdMateria());
                asignacionDocente.setAsignacionPK(asignacionPK);
                asignacionDocenteServices.registrarAsignacion(asignacionDocente);
                sd.setComplete();
            } catch (DataIntegrityViolationException e) {
                //message = getConstraintMessage(e.getMostSpecificCause().getMessage());
            } catch (Exception e) {
                message = ((flag)) ? "Error al realizar la asignacion" : "Error al realizar los cambios";
            }
            messageList.add(message);
        }
        return messageList;
    }

    @GetMapping("/consultar/{id}")
    public AsignacionDocente consultarAsignacion (@PathVariable(value = "id") String[] valores){
        AsignacionPK datos = new AsignacionPK();
        datos.setIdCurso(Long.parseLong(valores[0]));
        datos.setIdMateria(Long.parseLong(valores[1]));
        return asignacionDocenteServices.consultar(datos);
    }

}

