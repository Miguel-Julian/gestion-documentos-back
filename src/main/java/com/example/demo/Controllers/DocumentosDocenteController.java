package com.example.demo.Controllers;

import com.example.demo.Model.DocumentosDocente;
import com.example.demo.Services.DocumentosDocenteServices;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
@RequestMapping("/documentosDocente") //Activar servicios de documentosDocente
@SessionAttributes("documentosDocenteController")
public class DocumentosDocenteController {

    @Autowired
    DocumentosDocenteServices documentosDocenteServices;

    @GetMapping("/listar")
    public List<DocumentosDocente> listar(){
        //retorna todos los registros que hay en la tabla
        return documentosDocenteServices.Listar();
    }


    @PreAuthorize("hasRole('DOCENTE')")
    @PostMapping("/registrar")
    public List<String> registrarDocumentosDocente (@Valid @RequestBody DocumentosDocente documentosDocente, BindingResult bd, SessionStatus sd){
        //Verificar si hay errores
        List<String> messageList = new ArrayList<>();
        String message="";
        String[] field = {"archivoDocente", "estado"};
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
                message = (documentosDocente.getIdDocumentosDocente()==0)?"Se ha guardado la Actividad": "Datos actualizados";
                documentosDocenteServices.registrarDocumentosDocente(documentosDocente);
                documentosDocenteServices.pasarArchivo(documentosDocente.getArchivoDocente());
                sd.setComplete();
            } catch (DataIntegrityViolationException e) {
                //message = getConstraintMessage(e.getMostSpecificCause().getMessage());
            } catch (Exception e) {
                message = ((documentosDocente.getIdDocumentosDocente() == 0)) ? "Error al crear la Actividad" : "Error al realizar los cambios";
            }
            messageList.add(message);
        }
        return messageList;
    }

    @GetMapping("/consultar/{id}")
    public DocumentosDocente consultarDocumentosDocente (@PathVariable(value = "id") Long idDocumentosDocente){
        return documentosDocenteServices.consultarDocumentosDocente(idDocumentosDocente);
    }

    @PreAuthorize("hasRole('DOCENTE')")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFiles(@RequestParam("file") MultipartFile file){
        //Verificar si hay errores
        String message = "";
        try{
            documentosDocenteServices.guardarArchivo(file);
            message = "Subió el archivo con éxito: " + file.getOriginalFilename();
            return new ResponseEntity(message, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e);
            message = "No se pudo cargar el archivo.: " + file.getOriginalFilename() + "!";
            return new ResponseEntity(message, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/file/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename){
        Resource file = documentosDocenteServices.cargarArchivo(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\""+file.getFilename() + "\"").body(file);
    }

    @PreAuthorize("hasRole('DOCENTE')")
    @GetMapping("/delete/{filename:.+}")
    public String borrarArchivo(@PathVariable String filename) {
        return documentosDocenteServices.borrarArchivo(filename);
    }
    @GetMapping("/files")
    public ResponseEntity<List<String>> getFiles(){

        List<String> fileInfos = documentosDocenteServices.loadAll().map(path -> {
            String url = MvcUriComponentsBuilder.fromMethodName(DocumentosDocenteController.class, "getFile",
                    path.getFileName().toString()).build().toString();
            return url;
        }).collect(Collectors.toList());

        return new ResponseEntity(fileInfos, HttpStatus.OK);
    }
}
