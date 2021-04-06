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
@Table(name="Documentos_Estudiante")

public class DocumentosEstudiante implements Serializable {

    @EmbeddedId
    @Getter
    @Setter
    DocumentosEstudiantePK documentosEstudiantePK;

    @Getter
    @Setter
    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DocumentosDocente documentosDocente;


    @Getter
    @Setter
    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Estudiante estudiante;

    @Getter
    @Setter
    private float nota;

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
    @Size(min=3, max=250, message = "La descripcion de actividad debe estar entre 3 y 250 caracteres")
    private String comentario;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "fecha_entrega")
    private Date fechaEntrega;

    @Getter
    @Setter
    @Column
    private boolean estado;

}
