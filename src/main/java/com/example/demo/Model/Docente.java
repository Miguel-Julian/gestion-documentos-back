package com.example.demo.Model;

import com.example.demo.Security.Model.Usuario;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "Docentes")

public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "id_docente")
    private long idDocente;

    @Getter
    @Setter
    @Column(name = "dni_docente")
    private long dniDocente;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    private TipoDocumento tipoDocumento;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    private Usuario usuario;

    @Getter
    @Setter
    @Column(name = "nombre_docente")
    @Size(min = 3, max = 50, message = "El nombre del Docente debe esstar entre 3 y 50 caracteres")
    private String nombreDocente;

    @Getter
    @Setter
    @Column(name = "apellido_docente")
    @Size(min = 3, max = 50, message = "El apellido del Docente debe esstar entre 3 y 50 caracteres")
    private String apellidoDocente;

    @Getter
    @Setter
    @Column(name = "telefono_docente")
    @Size(min = 3, max = 50, message = "El telefono del Docente debe esstar entre 3 y 50 caracteres")
    private String telefonoDocente;

    @Getter
    @Setter
    @Column(name = "correo_docente")
    @Size(min = 3, max = 50, message = "El correo del Docente debe esstar entre 3 y 50 caracteres")
    private String correoDocente;

    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_nacimiento_docente")
    private Date fechaNacimientoDocente;

    @Getter
    @Setter
    @Column(name = "ciudad_docente")
    private String ciudadDocente;

    @Getter
    @Setter
    @Column
    private boolean estado;

    public Docente(){

    }
}
