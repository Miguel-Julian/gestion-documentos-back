package com.example.demo.Controllers;

import com.example.demo.Model.Usuario;
import com.example.demo.Services.UsuarioService;
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
@RequestMapping("/usuario")
@SessionAttributes("usuarioController")

public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/listar")
    public List<Usuario> listar(){
        return usuarioService.listarUsuarios();
    }

    @PostMapping("/registrar")
    public List<String> registrarUsuario (@Valid @RequestBody Usuario usuario, BindingResult bd, SessionStatus sd){
        //Verificar si hay errores
        List<String> messageList = new ArrayList<>();
        String message="";
        String[] field = {"nombreUsuario"};
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
                message = (usuario.getIdUsuario()==0)?"Se ha guardado el Usuario": "Datos actualizados";
                usuarioService.registrarUsuario(usuario);
                sd.setComplete();
            } catch (DataIntegrityViolationException e) {
                //message = getConstraintMessage(e.getMostSpecificCause().getMessage());
            } catch (Exception e) {
                message = ((usuario.getIdUsuario()==0)) ? "Error al crear el Usuario" : "Error al realizar los cambios";
            }
            messageList.add(message);
        }
        return messageList;
    }

    @GetMapping("/consultar/{id}")
    public Usuario consultarUsuario (@PathVariable(value = "id") Long idUsuario){
        return usuarioService.consultarUsuario(idUsuario);
    }
}
