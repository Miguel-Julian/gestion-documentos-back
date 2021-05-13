package com.example.demo.Controllers;


import com.example.demo.Model.AsignacionPK;
import com.example.demo.Model.Tema;
import com.example.demo.Services.AsignacionDocenteServices;
import com.example.demo.Services.TemaServices;
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
@RequestMapping("/tema")
@SessionAttributes("temaController")
public class TemaController {

    @Autowired
    TemaServices temaServices;

    @Autowired
    AsignacionDocenteServices asignacionDocenteServices;

    @GetMapping("/listar/{id}")
    public List<Tema> listar(@PathVariable(value = "id") String[] valores) {
        AsignacionPK datos = new AsignacionPK();
        datos.setIdCurso(Long.parseLong(valores[0]));
        datos.setIdMateria(Long.parseLong(valores[1]));
        return temaServices.listarTemas(asignacionDocenteServices.consultar(datos));
    }

    @GetMapping("/listarFalse/{id}")
    public List<Tema> listarFalse(@PathVariable(value = "id") String[] valores) {
        AsignacionPK datos = new AsignacionPK();
        datos.setIdCurso(Long.parseLong(valores[0]));
        datos.setIdMateria(Long.parseLong(valores[1]));
        return temaServices.listarTemasFalse(asignacionDocenteServices.consultar(datos));
    }

    @PreAuthorize("hasRole('DOCENTE')")
    @PostMapping("/registrar")
    public List<String> registrarTema (@Valid @RequestBody Tema tema, BindingResult bd, SessionStatus sd){
        //Verificar si hay errores
        List<String> messageList = new ArrayList<>();
        String message="";
        String[] field = {"nombreTema", "estado"};
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
                if(tema.getIdTema()==0){
                    message = "Se ha guardado el tema";
                    temaServices.registrarTema(tema,tema.getNombreTema());
                }else{
                    message =  "Datos actualizados";
                    //obtener el nombre anterior para cambiarlo en la ruta
                    String nombreAnterior = temaServices.consultarTema(tema.getIdTema()).getNombreTema();
                    temaServices.registrarTema(tema,nombreAnterior);
                }
                sd.setComplete();
            } catch (DataIntegrityViolationException e) {
                //message = getConstraintMessage(e.getMostSpecificCause().getMessage());
            } catch (Exception e) {
                message = ((tema.getIdTema() == 0)) ? "Error al crear la tema" : "Error al realizar los cambios";
            }
            messageList.add(message);
        }
        return messageList;
    }

    @PreAuthorize("hasRole('DOCENTE')")
    @PostMapping("/borrar")
    public List<String> borrarTema (@Valid @RequestBody Tema tema){
        List<String> messageList = new ArrayList<>();
        String message="";
        try {
            temaServices.borrarTema(tema);
            temaServices.borrarTodo(tema.getAsignacionDocente().getCurso().getNombreCurso()+"\\"+tema.getAsignacionDocente().getMateria().getNombreMateria()+"\\"+tema.getNombreTema());
            message = "Tema eliminado";
        }catch (Exception e) {
            message = "Error al borrar el tema";
        }
        System.out.println(message);
        messageList.add(message);
        return messageList;
    }

    @GetMapping("/consultar/{id}")
    public Tema consultarTema (@PathVariable(value = "id") Long idTema){
        return temaServices.consultarTema(idTema);
    }
}
