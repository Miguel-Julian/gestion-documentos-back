package com.example.demo.Services;

import com.example.demo.Dao.IDaoDocumentosEstudiante;
import com.example.demo.Model.DocumentosEstudiante;
import com.example.demo.Model.DocumentosEstudiantePK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Service
public class DocumentosEstudianteServices {

    @Autowired
    IDaoDocumentosEstudiante iDaoDocumentosEstudiante;

    private Path root = Paths.get("uploads");

    private  String rutaDestinoLocal = "c:\\temp\\directorio\\";
    public void registrarDocumentosEstudiante (DocumentosEstudiante documentosEstudiante){
        iDaoDocumentosEstudiante.save(documentosEstudiante);
    }

    public List<DocumentosEstudiante> listar (){
        return iDaoDocumentosEstudiante.findAllByEstado(true);
    }

    public DocumentosEstudiante consultar (DocumentosEstudiantePK datos){
        //long[] datos = {1,1};
        return iDaoDocumentosEstudiante.findById(datos).orElse(null);
    }

    //pasar el archivo ya que cuando se guarda en el metodo guardarArchivo() no se tiene la direccion final
    //por lo que solo se puede pasar despues cuando ya llega el objeto con la ruta
    public String pasarArchivo(String ruta,String nombre){
        String nombreNuevo = "";
        try {
            //Creacion de la carpta destino
            File direccionArchivo = new File(rutaDestinoLocal+ruta);
            direccionArchivo.mkdirs();
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
            throw new RuntimeException("No se puede guardar el archivo. Error " + e.getMessage());
        }
        return  nombreNuevo;
    }

    public void guardarArchivo(MultipartFile file) {
        try {
            //copy (que queremos copiar, a donde queremos copiar)
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException("No se puede guardar el archivo. Error " + e.getMessage());
        }
    }


    public Stream<Path> loadAll(){
        //Files.walk recorre nuestras carpetas (uploads) buscando los archivos
        // el 1 es la profundidad o nivel que queremos recorrer
        // :: Referencias a metodos
        // Relativize sirve para crear una ruta relativa entre la ruta dada y esta ruta
        try{
            return Files.walk(this.root,1).filter(path -> !path.equals(this.root))
                    .map(this.root::relativize);
        }catch (RuntimeException | IOException e){
            throw new RuntimeException("No se pueden cargar los archivos ");
        }
    }

    public Resource cargarArchivo(String filename)  {
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
}
