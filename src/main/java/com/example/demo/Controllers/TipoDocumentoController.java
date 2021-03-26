package com.example.demo.Controllers;

import com.example.demo.Model.Materia;
import com.example.demo.Model.TipoDocumento;
import com.example.demo.Services.MateriaServices;
import com.example.demo.Services.TipoDocumentoServices;
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
@RequestMapping("/tipoDocumento")
@SessionAttributes("tipoDocumentoController")

public class TipoDocumentoController {
    @Autowired
    TipoDocumentoServices tipoDocumentoServices;

    @GetMapping("/listar")
    public List<TipoDocumento> listar(){
        return tipoDocumentoServices.listar();
    }

    @PostMapping("/registrar")
    public List<String> registrarTipoDocumento (@Valid @RequestBody TipoDocumento tipoDocumento, BindingResult bd, SessionStatus sd){
        //Verificar si hay errores
        List<String> messageList = new ArrayList<>();
        String message="";
        String[] field = {"nombreTipoDocumento"};
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
                message = (tipoDocumento.getIdTipoDocumento()==0)?"Se ha guardado el tipo de documento": "Datos actualizados";
                tipoDocumentoServices.registrarTipoDoc(tipoDocumento);
                sd.setComplete();
            } catch (DataIntegrityViolationException e) {
                //message = getConstraintMessage(e.getMostSpecificCause().getMessage());
            } catch (Exception e) {
                message = ((tipoDocumento.getIdTipoDocumento()==0)) ? "Error al crear el tipo de documento" : "Error al realizar los cambios";
            }
            messageList.add(message);
        }
        return messageList;
    }

    @GetMapping("/consultar/{id}")
    public TipoDocumento consultarTipoDocumento (@PathVariable(value = "id") Long idTipoDocumento){
        return tipoDocumentoServices.consultarTipoDoc(idTipoDocumento);
    }
}
