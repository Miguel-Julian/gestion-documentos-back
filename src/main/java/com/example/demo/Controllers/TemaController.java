package com.example.demo.Controllers;


import com.example.demo.Model.Tema;
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

    @GetMapping("/listar")
    public List<Tema> listar(){
        return temaServices.listarTemas();
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
                message = (tema.getIdTema()==0)?"Se ha guardado el tema": "Datos actualizados";
                temaServices.registrarTema(tema);
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
    @GetMapping("/consultar/{id}")
    public Tema consultarTema (@PathVariable(value = "id") Long idTema){
        return temaServices.consultarTema(idTema);
    }
}
