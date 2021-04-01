package com.example.demo.Model;

import com.example.demo.Security.Model.Usuario;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="Estudiantes")
public class Estudiante implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "id_estudiante") //Si el campo tiene un nombre d  istinto en la BD
    private long idEstudiante;

    @Getter
    @Setter
    @Range(min=1, message = "El DNI del Estudiante debe ser mayor que 0")
    @Column(name = "dni_Estudiante") //Si el campo tiene un nombre distinto en la BD
    private long dniEstudiante;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    private TipoDocumento tipoDocumento;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    private Curso curso;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    private Usuario usuario;

    @Getter
    @Setter
    @Column(name = "nombre_estudiante")
    @Size(min=3, max=50, message = "El nombre del Estudiante debe esar entre 3 y 50 caracteres")
    private String nombreEstudiante;

    @Getter
    @Setter
    @Column(name = "apellido_estudiante")
    @Size(min=3, max=50, message = "El apellido del Estudiante debe esar entre 3 y 50 caracteres")
    private String apellidoEstudiante;

    @Getter
    @Setter
    @Column(name = "telefono_estudiante")
    @Size(min=3, max=50, message = "El telefono del Estudiante debe esar entre 3 y 50 caracteres")
    private String telefonoEstudiante;

    @Getter
    @Setter
    @Column(name = "correo_estudiante")
    @Size(min=3, max=70, message = "El correo del Estudiante debe esar entre 3 y 70 caracteres")
    private String correoEstudiante;

    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_nacimiento_estudiante")
    private Date fechaNacimientoEstudiante;

    @Getter
    @Setter
    @Column(name = "ciudad_estudiante")
    @Size(min=3, max=50, message = "La ciudad del Estudiante debe esar entre 3 y 50 caracteres")
    private String ciudadEstudiante;

    @Getter
    @Setter
    @Column
    private boolean estado;

    public Estudiante(){
    }

}

