package com.example.demo.Services;

import com.example.demo.Dao.IDaoEstudiante;
import com.example.demo.Model.*;
import com.example.demo.Security.Model.Usuario;
import com.example.demo.Security.Service.UsuarioService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

@Service
public class EstudianteServices {
    @Autowired
    IDaoEstudiante iDaoEstudiante;

    @Autowired
    CursoServices cursoServices;

    @Autowired
    TipoDocumentoServices tipoDocumentoServices;

    @Autowired
    UsuarioService usuarioService;

    private long dni =0;

    public void registrarEstudiante (Estudiante estudiante) {
        iDaoEstudiante.save(estudiante);
    }

    public List<Estudiante> Listar(){
        return iDaoEstudiante.findAllByEstado(true);
    }

    public Estudiante consultarEstudiante(long idEstudiante){
        return iDaoEstudiante.findById(idEstudiante).orElse(null);
    }


    public String save (MultipartFile file) {
        String message = "";
        try {
            List<Estudiante> Estudiantes = excelToEstudiantes(file.getInputStream());
            if(dni == 0){
                iDaoEstudiante.saveAll(Estudiantes);
                message="!Subió el archivo con éxito!";
            }else{
                message = "El numero de tarjeta: " +dni+ " Ya existe";
            }
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
        return message;
    }


    public  List<Estudiante> excelToEstudiantes(InputStream is) {
        List<Curso> Cursos = cursoServices.Listar();
        List<TipoDocumento> tipoDocumentos = tipoDocumentoServices.listar();
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet("Estudiante");
            Iterator<Row> rows = sheet.iterator();
            List<Estudiante> Estudiantes = new ArrayList<Estudiante>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Estudiante estudiante = new Estudiante();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            estudiante.setDniEstudiante((long) currentCell.getNumericCellValue());
                            break;

                        case 1:
                            String tipoDocumento = currentCell.getStringCellValue();
                            for(int i = 0; i < tipoDocumentos.size(); i++){
                                if (tipoDocumentos.get(i).getNombreTipoDocumento().equals(tipoDocumento)){
                                    estudiante.setTipoDocumento(tipoDocumentos.get(i));
                                }
                            }
                            break;

                        case 2:
                            estudiante.setNombreEstudiante(currentCell.getStringCellValue());
                            break;

                        case 3:
                            estudiante.setApellidoEstudiante(currentCell.getStringCellValue());
                            break;

                        case 4:
                            estudiante.setTelefonoEstudiante(currentCell.getStringCellValue());
                            break;

                        case 5:
                            estudiante.setCorreoEstudiante(currentCell.getStringCellValue());
                            break;

                        case 6:
                            estudiante.setFechaNacimientoEstudiante(currentCell.getDateCellValue());
                            break;

                        case 7:
                            estudiante.setCiudadEstudiante(currentCell.getStringCellValue());
                            break;

                        case 8:
                            String nombreCurso = currentCell.getStringCellValue();
                            for(int i = 0; i < Cursos.size(); i++){
                                if (Cursos.get(i).getNombreCurso().equals(nombreCurso)){
                                    estudiante.setCurso(Cursos.get(i));
                                }
                            }
                            estudiante.setEstado(true);
                            if(verificarDNI(estudiante.getDniEstudiante()) !=0){
                                dni =verificarDNI(estudiante.getDniEstudiante());
                            }
                            break;

                        default:
                            break;
                    }

                    cellIdx++;
                }
                Estudiantes.add(estudiante);
            }
            workbook.close();
            if(dni !=0){
                Estudiantes.clear();
            }else{
                for(int i = 0; i < Estudiantes.size(); i++) {
                    Estudiantes.get(i).setUsuario(DatosUsuario(Estudiantes.get(i)));
                    usuarioService.registrarUsuario(Estudiantes.get(i).getUsuario());
                }
            }
            return Estudiantes;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public Usuario DatosUsuario (Estudiante estudiante){
        Usuario usuario = new Usuario();
        if (estudiante.getIdEstudiante()!=0){
            usuario = estudiante.getUsuario();
        }
        List<Usuario> usuarios = usuarioService.listarUsuarios();

        usuario.setContrasenia(estudiante.getDniEstudiante()+"");
        usuario.setRol("ROLE_ESTUDIANTE");
        usuario.setEstado(true);

        StringTokenizer tokens = new StringTokenizer(estudiante.getApellidoEstudiante());
        String primeraApellido = tokens.nextToken();
        String nombreUsuario = estudiante.getNombreEstudiante().charAt(0)+primeraApellido;
        int cont =1;
        for(int i = 0; i < usuarios.size(); i++){
            if (usuarios.get(i).getNombreUsuario().equals(nombreUsuario)){
                nombreUsuario = nombreUsuario.substring(0, nombreUsuario.length()-1);
                nombreUsuario = nombreUsuario+cont;
                cont++;
                i=0;
            }
        }
        usuario.setNombreUsuario(nombreUsuario);

        return usuario;
    }

    private long verificarDNI(long DNI){
        long flag = 0;
        List<Estudiante> estudiantes = Listar();
        for(int i = 0; i < estudiantes.size(); i++){
            if(estudiantes.get(i).getDniEstudiante() == DNI){
                flag = DNI;
            }
        }
        return flag;
    }
}
