package com.example.demo.Services;

import com.example.demo.Dao.IDaoDocumentosEstudiante;
import com.example.demo.Model.*;
import lombok.Getter;
import lombok.Setter;
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

    @Autowired
    TemaServices temaServices;

    @Autowired
    DocumentosDocenteServices documentosDocenteServices;

    @Autowired
    AsignacionDocenteServices asignacionDocenteServices;

    private Path root = Paths.get("uploads");

    @Getter
    private  String rutaDestinoLocal = "c:\\temp\\directorio\\";

    @Setter
    private String direccion;

    public void registrarDocumentosEstudiante (DocumentosEstudiante documentosEstudiante){
        iDaoDocumentosEstudiante.save(documentosEstudiante);
    }

    public List<DocumentosEstudiante> listar (){
        return iDaoDocumentosEstudiante.findAllByEstado(true);
    }

    public List<DocumentosEstudiante> listarByDocDocente (DocumentosDocente documentosDocente){
        return iDaoDocumentosEstudiante.findAllByDocumentosDocente(documentosDocente);
    }

    public DocumentosEstudiante consultar (DocumentosEstudiantePK datos){
        //long[] datos = {1,1};
        return iDaoDocumentosEstudiante.findById(datos).orElse(null);
    }

    //pasar el archivo ya que cuando se guarda en el metodo guardarArchivo() no se tiene la direccion final
    //por lo que solo se puede pasar despues, cuando ya llega el objeto con la ruta
    public String pasarArchivo(String ruta,String nombre,String id){
        String nombreNuevo = "";
        try {
            Path RutaFinal = Paths.get(rutaDestinoLocal+ruta);
            //cambiar el nombre para agregar-#id- para que no se repitan
            File nombreOriginal = new  File(this.root.toString()+"\\"+nombre);
            nombreNuevo = "-"+id+"-"+nombre;
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


    public String borrarArchivo(String filename){
        try {
            Files.deleteIfExists(this.root.resolve(filename));
            return "Borrado";
        }catch (IOException e){
            e.printStackTrace();
            return "Error Borrando: "+filename;
        }
    }

    public float notaDefinitiva(long idCurso,long idMateria,long idEstudiante){
        float sumaNotas = 0;
        int count =0;
        AsignacionPK pk = new AsignacionPK();
        pk.setIdCurso(idCurso);
        pk.setIdMateria(idMateria);
        List<Tema> listTema = temaServices.listarTemas(asignacionDocenteServices.consultar(pk));
        for (int k =0;k<listTema.size();k++) {
            List<DocumentosDocente> listDocDocente = documentosDocenteServices.Listar(listTema.get(k));
            for (int i = 0; i < listDocDocente.size(); i++) {
                List<DocumentosEstudiante> listEstudiante = iDaoDocumentosEstudiante.findAllByDocumentosDocente(listDocDocente.get(i));
                for (int j = 0; j < listEstudiante.size(); j++) {
                    if (listEstudiante.get(j).getDocumentosEstudiantePK().getIdEstudiante() == idEstudiante) {
                        sumaNotas += listEstudiante.get(j).getNota();
                        count++;
                    }
                }
            }
        }
        return sumaNotas/count;
    }
}
