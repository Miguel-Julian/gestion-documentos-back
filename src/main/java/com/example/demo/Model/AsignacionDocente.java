package com.example.demo.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "asignacion_docente")
//@IdClass(value = AsignacionDocentePK.class)

public class AsignacionDocente implements Serializable {
    @EmbeddedId
    @Getter
    @Setter
    AsignacionPK asignacionPK;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    private Curso curso;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    private Materia materia;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    private Docente docente;

    @Getter
    @Setter
    @Column
    private boolean estado;
}


