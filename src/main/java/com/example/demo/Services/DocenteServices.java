package com.example.demo.Services;

import com.example.demo.Dao.IDaoDocente;
import com.example.demo.Model.Docente;
import com.example.demo.Model.TipoDocumento;
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
public class DocenteServices {
    @Autowired
    IDaoDocente iDaoDocente;

    @Autowired
    TipoDocumentoServices tipoDocumentoServices;

    @Autowired
    UsuarioService usuarioService;

    private long dni =0;

    public List<Docente> listarDocentes (){
        return iDaoDocente.findAllByEstado(true);
    }

    public void registrarDocente (Docente docente){
        iDaoDocente.save(docente);
    }

    public Docente consultarDocente (long idDocente){
        return iDaoDocente.findById(idDocente).orElse(null);
    }

    public String save (MultipartFile file) {
        String message = "";
        try {
            List<Docente> Docentes = excelToDocentes(file.getInputStream());
            if(dni == 0){
                iDaoDocente.saveAll(Docentes);
                message="!Subió el archivo con éxito!";
            }else{
                message = "El numero de tarjeta: " +dni+ " Ya existe";
            }

        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
        return message;
    }


    public  List<Docente> excelToDocentes(InputStream is) {
        List<TipoDocumento> tipoDocumentos = tipoDocumentoServices.listar();
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet("Docente");
            Iterator<Row> rows = sheet.iterator();
            List<Docente> Docentes = new ArrayList<Docente>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Docente docente = new Docente();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            docente.setDniDocente((long) currentCell.getNumericCellValue());
                            break;

                        case 1:
                            String tipoDocumento = currentCell.getStringCellValue();
                            for(int i = 0; i < tipoDocumentos.size(); i++){
                                if (tipoDocumentos.get(i).getNombreTipoDocumento().equals(tipoDocumento)){
                                    docente.setTipoDocumento(tipoDocumentos.get(i));
                                }
                            }
                            break;

                        case 2:
                            docente.setNombreDocente(currentCell.getStringCellValue());
                            break;

                        case 3:
                            docente.setApellidoDocente(currentCell.getStringCellValue());
                            break;

                        case 4:
                            docente.setTelefonoDocente(currentCell.getStringCellValue());
                            break;

                        case 5:
                            docente.setCorreoDocente(currentCell.getStringCellValue());
                            break;

                        case 6:
                            docente.setFechaNacimientoDocente(currentCell.getDateCellValue());
                            break;

                        case 7:
                            docente.setCiudadDocente(currentCell.getStringCellValue());
                            docente.setEstado(true);
                            if(verificarDNI(docente.getDniDocente()) !=0){
                                dni =verificarDNI(docente.getDniDocente());
                            }
                            break;

                        default:
                            break;
                    }

                    cellIdx++;
                }
                Docentes.add(docente);
            }
            workbook.close();
            if(dni !=0){
                Docentes.clear();
            }else{
                for(int i = 0; i < Docentes.size(); i++) {
                    Docentes.get(i).setUsuario(DatosDocente(Docentes.get(i)));
                    usuarioService.registrarUsuario(Docentes.get(i).getUsuario());
                }
            }

            return Docentes;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
    public Usuario DatosDocente (Docente docente){
        Usuario usuario = new Usuario();
        if (docente.getIdDocente()!=0){
            usuario = docente.getUsuario();
        }
        List<Usuario> usuarios = usuarioService.listarUsuarios();

        usuario.setContrasenia(docente.getDniDocente()+"");
        usuario.setRol("ROLE_DOCENTE");
        usuario.setEstado(true);

        StringTokenizer tokens = new StringTokenizer(docente.getApellidoDocente());
        String primeraApellido = tokens.nextToken();
        String nombreUsuario = docente.getNombreDocente().charAt(0)+primeraApellido;
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
        List<Docente> docentes = listarDocentes();
        for(int i = 0; i < docentes.size(); i++){
            if(docentes.get(i).getDniDocente() == DNI){
                flag = DNI;
            }
        }
        return flag;
    }
}
