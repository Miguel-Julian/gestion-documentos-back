package com.example.demo.Controllers;

import com.example.demo.Model.DocumentosDocente;
import com.example.demo.Services.DocumentosDocenteServices;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
    public DocumentosDocente consultarCalificacion (@PathVariable(value = "id") Long idDocumentosDocente){
        return documentosDocenteServices.consultarDocumentosDocente(idDocumentosDocente);
    }

    @PostMapping("/upload")
    public String uploadFiles(@RequestParam("file") MultipartFile file){
        //Verificar si hay errores
        documentosDocenteServices.setRoot(Paths.get("c:\\temp\\directorio"));
        String message = "";
        try{
            documentosDocenteServices.guardarArchivo(file);
            return message = "Subió el archivo con éxito: " + file.getOriginalFilename();
        }catch (Exception e){
            System.out.println(e);
            return message = "No se pudo cargar el archivo.: " + file.getOriginalFilename() + "!";
        }
    }


    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename){
        Resource file = documentosDocenteServices.cargarArchivo(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\""+file.getFilename() + "\"").body(file);
    }

    @GetMapping("/delete/{filename:.+}")
    public String borrarArchivo(@PathVariable String filename) {
        return documentosDocenteServices.borrarArchivo(filename);
    }

}
