package com.example.demo.Controllers;

import com.example.demo.Model.DocumentosDocente;
import com.example.demo.Model.Tema;
import com.example.demo.Services.DocumentosDocenteServices;

import com.example.demo.Services.TemaServices;
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

import java.nio.file.Files;
import java.nio.file.Paths;
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

    @Autowired
    TemaServices temaServices;

    @GetMapping("/listar/{id}")
    public List<DocumentosDocente> listar(@PathVariable(value = "id") long idTema){
        return documentosDocenteServices.Listar(temaServices.consultarTema(idTema));
    }



    @PreAuthorize("hasRole('DOCENTE')")
    @PostMapping("/registrar")
    public List<String> registrarDocumentosDocente (@Valid @RequestBody DocumentosDocente documentosDocente, BindingResult bd, SessionStatus sd){
        //Verificar si hay errores
        List<String> messageList = new ArrayList<>();
        String message="";
        String[] field = {"nombreActividad", "estado"};
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
                if(documentosDocente.getIdDocumentosDocente() == 0){
                    message = "Se ha guardado la Actividad";
                    if(documentosDocente.getNombreArchivo() != ""){
                        documentosDocente.setNombreArchivo(documentosDocenteServices.pasarArchivo(documentosDocente.getRutaArchivo(),documentosDocente.getNombreArchivo()));
                    }
                }else{
                    DocumentosDocente doc = documentosDocenteServices.consultarDocumentosDocente(documentosDocente.getIdDocumentosDocente());// creado para verificar si se cambio el archivo o el nombre
                    message = "Datos actualizados";
                    if(!documentosDocente.getNombreArchivo().equals(doc.getNombreArchivo())){
                        if(doc.getNombreArchivo() == "" && documentosDocente.getNombreArchivo() !=""){
                            Files.deleteIfExists(Paths.get("c:\\temp\\directorio\\"+doc.getRutaArchivo()+documentosDocente.getNombreArchivo()));
                        }else if(doc.getNombreArchivo() != "" && documentosDocente.getNombreArchivo() ==""){
                            Files.deleteIfExists(Paths.get("c:\\temp\\directorio\\"+doc.getRutaArchivo()+doc.getNombreArchivo()));
                        }else {
                            Files.deleteIfExists(Paths.get("c:\\temp\\directorio\\"+doc.getRutaArchivo()+doc.getNombreArchivo()));
                        }
                    }
                    if(documentosDocente.getTema().getNombreTema() == ""){
                        documentosDocente.setTema(doc.getTema());
                        documentosDocente.setNombreArchivo(documentosDocenteServices.pasarArchivo(doc.getRutaArchivo(),documentosDocente.getNombreArchivo()));
                    }
                    documentosDocenteServices.cambiarNombre(doc.getRutaArchivo(),documentosDocente.getRutaArchivo());//al tema
                }
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
    public DocumentosDocente consultarDocumentosDocente (@PathVariable(value = "id") Long idDocumentosDocente){
        return documentosDocenteServices.consultarDocumentosDocente(idDocumentosDocente);
    }

    @PreAuthorize("hasRole('DOCENTE')")
    @PostMapping("/borrar")
    public List<String> borrarDocumentosDocente (@Valid @RequestBody DocumentosDocente documentosDocente){
        List<String> messageList = new ArrayList<>();
        String message="";
        try {
            documentosDocenteServices.borrarDocumentosDocente(documentosDocente);
            documentosDocenteServices.borrarTodo(documentosDocente.getRutaArchivo());
            message = "Documento Docente eliminado";
        }catch (Exception e) {
            message = "Error al borrar el Documento Docente";
        }
        System.out.println(message);
        messageList.add(message);
        return messageList;
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


    @GetMapping("/files/{id}")
    public ResponseEntity<List<String>> getFiles(@PathVariable(value = "id") Long idDocumentosDocente){
        DocumentosDocente a = documentosDocenteServices.consultarDocumentosDocente(idDocumentosDocente);
        documentosDocenteServices.setDireccion(a.getRutaArchivo());
        List<String> fileInfos = documentosDocenteServices.loadAll(a.getRutaArchivo()).map(path -> {
            String url = MvcUriComponentsBuilder.fromMethodName(DocumentosDocenteController.class, "getFile",
                    path.getFileName().toString()).build().toString();
            return url;
        }).collect(Collectors.toList());

        return new ResponseEntity(fileInfos, HttpStatus.OK);
    }
}
