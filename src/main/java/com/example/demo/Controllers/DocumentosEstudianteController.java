package com.example.demo.Controllers;


import com.example.demo.Model.DocumentosEstudiante;
import com.example.demo.Services.DocumentosEstudianteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*") //Recibe peticiones
@RequestMapping("/documentosEstudiante") //Activar servicios de documentosDocente
@SessionAttributes("documentosEstudianteController")
public class DocumentosEstudianteController {
    @Autowired
    DocumentosEstudianteServices documentosEstudianteServices;

    @GetMapping("/listar")
    public List<DocumentosEstudiante> listar(){
        //retorna todos los registros que hay en la tabla
        return documentosEstudianteServices.listar();
    }

}
