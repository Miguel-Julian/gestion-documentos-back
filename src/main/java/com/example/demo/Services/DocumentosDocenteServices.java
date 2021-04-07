package com.example.demo.Services;


import com.example.demo.Dao.IDaoDocumentosDocente;
import com.example.demo.Model.DocumentosDocente;

import com.example.demo.Model.Tema;

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

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Service
public class DocumentosDocenteServices {
    @Autowired
    IDaoDocumentosDocente iDaoDocumentosDocente;

    private Path root = Paths.get("uploads");

    private  String rutaDestinoLocal = "c:\\temp\\directorio\\";

    @Setter
    private String direccion;

    public void registrarDocumentosDocente (DocumentosDocente documentosDocente) {
        crearCarpetas(documentosDocente.getRutaArchivo());
        iDaoDocumentosDocente.save(documentosDocente);
    }

    public List<DocumentosDocente> Listar(Tema tema){
        return iDaoDocumentosDocente.findAllByTema(tema);
    }

    public DocumentosDocente consultarDocumentosDocente(long idDocumentosDocente){
        return iDaoDocumentosDocente.findById(idDocumentosDocente).orElse(null);
    }

    public void borrarDocumentosDocente (DocumentosDocente documentosDocente) {
        crearCarpetas(documentosDocente.getRutaArchivo());
        iDaoDocumentosDocente.delete(documentosDocente);
    }
    //pasar el archivo ya que cuando se guarda en el metodo guardarArchivo() no se tiene la direccion final
    //por lo que solo se puede pasar despues, cuando ya llega el objeto con la ruta
    public String pasarArchivo(String ruta,String nombre){
        String nombreNuevo = "";
        try {
            crearCarpetas(ruta);
            Path RutaFinal = Paths.get(rutaDestinoLocal+ruta);
            //cambiar el nombre para agregar-#id- para que no se repitan
            File nombreOriginal = new  File(this.root.toString()+"\\"+nombre);
            nombreNuevo = "-"+ruta.split("-")[1]+"-"+nombre;
            File newName = new  File(this.root.toString()+"\\"+nombreNuevo);
            nombreOriginal.renameTo(newName);
            //guardar el archivo en la nueva ruta
            Files.copy(Paths.get(this.root.toString()+"\\"+nombreNuevo), RutaFinal.resolve(nombreNuevo));
            //borrar el archivo en la carpeta "uploads"
            System.out.println(borrarArchivo(nombreNuevo));
        }catch (Exception e) {
            throw new RuntimeException("No se puede guardar el archivo. Error: " + e.getMessage());
        }
        return  nombreNuevo;

    }

    public void guardarArchivo(MultipartFile file) {
        try {
            //copy (que queremos copiar, a donde queremos copiar)
            FileSystemUtils.deleteRecursively(this.root);//borrar  carpeta temporal con todos sus archivos
            Files.createDirectory(this.root);// creacion de la carpeta temporal (para guardarArchivo())
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException("No se puede guardar el archivo. Error " + e.getMessage());
        }
    }


    public Stream<Path> loadAll(String ruta){
        //Files.walk recorre nuestras carpetas buscando los archivos
        // el 1 es la profundidad o nivel que queremos recorrer
        // :: Referencias a metodos
        // Relativize sirve para crear una ruta relativa entre la ruta dada y esta ruta
        Path ds = Paths.get(rutaDestinoLocal+ruta);
        try{
            return Files.walk(ds,1).filter(path ->
                    !Files.isDirectory(path) && !path.equals(ds)
            ).map(ds::relativize);
        }catch (RuntimeException | IOException e){
            throw new RuntimeException("No se pueden cargar los archivos ");
        }
    }

    public Resource cargarArchivo(String filename)  {
        try {
            Path root1 = Paths.get(rutaDestinoLocal+direccion);
            Path file = root1.resolve(filename);
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

    public void borrarTodo(String ruta) {
        FileSystemUtils.deleteRecursively(new File(rutaDestinoLocal+ruta));
    }


    public String borrarArchivo(String filename){
        try {
            Files.deleteIfExists(this.root.resolve(filename));
            return "Borrado";
        }catch (IOException e){
            e.printStackTrace();
            return "Error Borrando: "+filename;
        }
    }

    public void crearCarpetas(String ruta){
        try {
            File direccionArchivo = new File(rutaDestinoLocal+ruta);//Creacion de la carpta para guardar el archivo
            File carpetaArchivosEstudiantes = new File(rutaDestinoLocal+ruta+"Archivos Estudiantes");//Creacion de la carpeta donde se van a guardar los archivos que cargen los estudiantes
            direccionArchivo.mkdirs();
            carpetaArchivosEstudiantes.mkdirs();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void cambiarNombre(String Anterior, String nuevo){
        //cambiar el nombre de la carpeta si entra a modificar
        File nombreOriginal = new  File(this.rutaDestinoLocal+"\\"+Anterior);
        File newName = new  File(this.rutaDestinoLocal+nuevo);
        nombreOriginal.renameTo(newName);
    }
}