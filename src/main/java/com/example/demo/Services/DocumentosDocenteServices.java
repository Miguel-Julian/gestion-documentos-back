package com.example.demo.Services;


import com.example.demo.Dao.IDaoDocumentosDocente;
import com.example.demo.Model.DocumentosDocente;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.net.MalformedURLException;

import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Files;

import java.util.List;

@Service
public class DocumentosDocenteServices {
    @Autowired
    IDaoDocumentosDocente iDaoDocumentosDocente;

    @Getter
    @Setter
    private Path root;

    public void registrarDocumentosDocente (DocumentosDocente documentosDocente) {
        iDaoDocumentosDocente.save(documentosDocente);
    }

    public List<DocumentosDocente> Listar(){
        return iDaoDocumentosDocente.findAllByEstado(true);
    }

    public DocumentosDocente consultarDocumentosDocente(long idDocumentosDocente){
        return iDaoDocumentosDocente.findById(idDocumentosDocente).orElse(null);
    }

    public void crearCarpeta() {

    }

    public void guardarArchivo(MultipartFile file) {
        try {
            //Creacion de carpeta destino
            File directorio = new File(root.toString());
            directorio.mkdirs();

            //copy (que queremos copiar, a donde queremos copiar)
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException("No se puede guardar el archivo. Error " + e.getMessage());
        }
    }

    public Resource cargarArchivo(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if(resource.exists() || resource.isReadable()){
                return resource;
            }else{
                throw new RuntimeException("No se puede leer el archivo ");
            }

        }catch (MalformedURLException e){
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void borrarTodo() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }


    public String borrarArchivo(String filename){
        try {
            Boolean delete = Files.deleteIfExists(this.root.resolve(filename));
            return "Borrado";
        }catch (IOException e){
            e.printStackTrace();
            return "Error Borrando: "+filename;
        }
    }


}