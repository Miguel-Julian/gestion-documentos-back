
package com.example.demo.Model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="Documentos_Docente")
public class DocumentosDocente implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "id_documentos_docente") //Si el campo tiene un nombre distinto en la BD
    private long idDocumentosDocente;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tema tema;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    private Calificacion calificacion;

    @Getter
    @Setter
    @Column(name = "nombre_Actividad")
    @Size(min=3, max=50, message = "El nombre de la Actividad debe estar entre 3 y 50 caracteres")
    private String nombreActividad;

    @Getter
    @Setter
    @Column(name = "tipo_actividad")
    @Size(min=3, max=30, message = "El tipo de actividad debe estar entre 3 y 30 caracteres")
    private String tipoActividad;

    @Getter
    @Setter
    @Column(name = "descripcion_actividad")
    @Size(min=3, max=255, message = "La descripcion de actividad debe estar entre 3 y 255 caracteres")
    private String descripcionActividad;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "fecha_limite")
    private Date fechaLimite;

    @Getter
    @Setter
    @Column(name = "ruta_Archivo")
    private String rutaArchivo;

    @Getter
    @Setter
    @Column(name = "nombre_Archivo")
    private String nombreArchivo;

    @Getter
    @Setter
    @Column
    private boolean estado;

    public DocumentosDocente() {
    }
}

