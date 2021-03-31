package com.example.demo.Controllers;

import com.example.demo.Model.Estudiante;
import com.example.demo.Services.EstudianteServices;
import com.example.demo.Security.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*") //Recibe peticiones
@RequestMapping("/estudiante") //Activar servicios del estudiante
@SessionAttributes("estudianteController")


public class EstudianteController {
    @Autowired
    EstudianteServices estudianteServices;

    @Autowired
    UsuarioService usuarioService;


    @GetMapping("/listar")
    public List<Estudiante> listar(){
        //retorna todos los registros que hay en la tabla
        return estudianteServices.Listar();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registrar")
    public List<String> registrarEstudiante (@Valid @RequestBody Estudiante estudiante, BindingResult bd, SessionStatus sd){
        //Verificar si hay errores
        List<String> messageList = new ArrayList<>();
        String message="";
        String[] field = {"nombreEstudiante", "estado"};
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
                estudiante.setUsuario(estudianteServices.DatosUsuario(estudiante));
                usuarioService.registrarUsuario(estudiante.getUsuario());
                message = (estudiante.getIdEstudiante()==0)?"Se ha guardado el Estudiante": "Datos actualizados sjjasd";
                estudianteServices.registrarEstudiante(estudiante);
                sd.setComplete();
            } catch (DataIntegrityViolationException e) {
                //message = getConstraintMessage(e.getMostSpecificCause().getMessage());
            } catch (Exception e) {
                message = ((estudiante.getIdEstudiante() == 0)) ? "Error al crear el estudiante" : "Error al realizar los cambios";
            }
            messageList.add(message);
        }
        return messageList;
    }

    @GetMapping("/consultar/{id}")
    public Estudiante consultarEstudiante (@PathVariable(value = "id") Long idEstudiante){
        return estudianteServices.consultarEstudiante(idEstudiante);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload")
    public List<String>  uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "¡Sube un archivo de Excel!";
        List<String> messageList = new ArrayList<>();
        String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        if (TYPE.equals(file.getContentType())) {
            try {
                message= estudianteServices.save(file);
            } catch (Exception e) {
                System.out.println(e);
                message = "No se pudo cargar el archivo.: " + file.getOriginalFilename() + "!";
            }
        }
        messageList.add(message);
        return messageList;
    }
}

