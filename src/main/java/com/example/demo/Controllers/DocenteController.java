package com.example.demo.Controllers;

import com.example.demo.Model.Docente;
import com.example.demo.Model.Estudiante;
import com.example.demo.Services.DocenteServices;
import com.example.demo.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/docente")
@SessionAttributes("/docenteController")
public class DocenteController {
    @Autowired
    DocenteServices docenteServices;
    @Autowired
    UsuarioService usuarioService;

    @GetMapping("listarDocentes")
    public List<Docente> listar(){
        return docenteServices.listarDocentes();
    }

    @PostMapping("/registrar")
    public List<String> registrarDocente (@Valid @RequestBody Docente docente, BindingResult bd, SessionStatus sd){
        //Verificar si hay errores
        List<String> messageList = new ArrayList<>();
        String message="";
        String[] field = {"nombreDocente", "estado"};
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
                docente.setUsuario(docenteServices.DatosDocente(docente));
                usuarioService.registrarUsuario(docente.getUsuario());
                message = (docente.getIdDocente()==0)?"Se ha guardado el Docente": "Datos actualizados";
                docenteServices.registrarDocente(docente);
                sd.setComplete();
            } catch (DataIntegrityViolationException e) {
                //message = getConstraintMessage(e.getMostSpecificCause().getMessage());
            } catch (Exception e) {
                message = ((docente.getIdDocente()==0)) ? "Error al crear el Docente" : "Error al realizar los cambios";
            }
            messageList.add(message);
        }
        return messageList;
    }

    @GetMapping("/consultar/{id}")
    public Docente consultarDocente (@PathVariable(value = "id") Long idDocente){
        return docenteServices.consultarDocente(idDocente);
    }


    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "¡Sube un archivo de Excel!";
        String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        if (TYPE.equals(file.getContentType())) {
            try {
                docenteServices.save(file);
                message = "Subió el archivo con éxito: " + file.getOriginalFilename();
            } catch (Exception e) {
                System.out.println(e);
                message = "No se pudo cargar el archivo.: " + file.getOriginalFilename() + "!";
            }
        }
        return message;
    }
}
