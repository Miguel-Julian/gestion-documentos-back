package com.example.demo.Controllers;

import com.example.demo.Model.DocumentosEstudiante;
import com.example.demo.Model.DocumentosEstudiantePK;
import com.example.demo.Services.DocumentosDocenteServices;
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
import java.nio.file.Files;
import java.nio.file.Paths;
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

    @Autowired
    DocumentosDocenteServices documentosDocenteServices;

    @GetMapping("/listar")
    public List<DocumentosEstudiante> listar(){
        //retorna todos los registros que hay en la tabla
        return documentosEstudianteServices.listar();
    }

    @GetMapping("/listarByDocDocente/{id}")
    public List<DocumentosEstudiante> listarByDocDocente(@PathVariable(value = "id") long id){
        return documentosEstudianteServices.listarByDocDocente(documentosDocenteServices.consultarDocumentosDocente(id));
    }

    @PreAuthorize("hasRole('DOCENTE') OR hasRole('ESTUDIANTE')")
    @PostMapping("/registrar")
    public List<String> registrarDocDocente (@Valid @RequestBody DocumentosEstudiante documentoEstudiante, BindingResult bd, SessionStatus sd){
        //verificar si se esta guardando o actualizando
        List<DocumentosEstudiante> documentosEstudiantes = documentosEstudianteServices.listar();
        //Verificar si hay errores
        List<String> messageList = new ArrayList<>();
        String message="";
        String[] field = {"comentario", "estado"};
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
                message = "Se ha asignado correctamente el Estudiante y la Actividad";
                DocumentosEstudiantePK documentosEstudiantePK = new DocumentosEstudiantePK();
                documentosEstudiantePK.setIdDocumentosDocente(documentoEstudiante.getDocumentosDocente().getIdDocumentosDocente());
                documentosEstudiantePK.setIdEstudiante(documentoEstudiante.getEstudiante().getIdEstudiante());
                documentoEstudiante.setDocumentosEstudiantePK(documentosEstudiantePK);
                documentoEstudiante.setNombreArchivo(documentosEstudianteServices.pasarArchivo(documentoEstudiante.getRutaArchivo(),documentoEstudiante.getNombreArchivo()));
                documentosEstudianteServices.registrarDocumentosEstudiante(documentoEstudiante);
                sd.setComplete();
            } catch (DataIntegrityViolationException e) {
                //message = getConstraintMessage(e.getMostSpecificCause().getMessage());
            } catch (Exception e) {
                message = "Error al realizar los cambios";
            }
            messageList.add(message);
        }
        return messageList;
    }

    @GetMapping("/consultar/{id}")
    public DocumentosEstudiante consultarDocEstudiante (@PathVariable(value = "id") String[] valores){
        DocumentosEstudiantePK documentosEstudiantePK = new DocumentosEstudiantePK();
        documentosEstudiantePK.setIdDocumentosDocente(Long.parseLong(valores[0]));
        documentosEstudiantePK.setIdEstudiante(Long.parseLong(valores[1]));
        return documentosEstudianteServices.consultar(documentosEstudiantePK);
    }



    @PreAuthorize("hasRole('ESTUDIANTE')")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFiles(@RequestParam("file") MultipartFile file){
        //Verificar si hay errores
        String message = "";
        try{
            documentosEstudianteServices.guardarArchivo(file);
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
        Resource file = documentosEstudianteServices.cargarArchivo(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\""+file.getFilename() + "\"").body(file);
    }


    @GetMapping("/files/{id}")
    public ResponseEntity<List<String>> getFiles(@PathVariable(value = "id") String[] valores){
        DocumentosEstudiantePK documentosEstudiantePK = new DocumentosEstudiantePK();
        documentosEstudiantePK.setIdDocumentosDocente(Long.parseLong(valores[0]));
        documentosEstudiantePK.setIdEstudiante(Long.parseLong(valores[1]));
        DocumentosEstudiante a = documentosEstudianteServices.consultar(documentosEstudiantePK);
        documentosEstudianteServices.setDireccion(a.getRutaArchivo());
        List<String> fileInfos = documentosEstudianteServices.loadAll(a.getRutaArchivo()).map(path -> {
            String url = MvcUriComponentsBuilder.fromMethodName(DocumentosEstudianteController.class, "getFile",
                    path.getFileName().toString()).build().toString();
            return url;
        }).collect(Collectors.toList());

        return new ResponseEntity(fileInfos, HttpStatus.OK);
    }
}
