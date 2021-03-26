package com.example.demo.Model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name="Cursos")

public class Curso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "id_curso") //Si el campo tiene un nombre d  istinto en la BD
    private long idCurso;

    @Getter
    @Setter
    @Column(name = "nombre_curso")
    @Size(min=3, max=15, message = "El nombre del curso debe esar entre 3 y 15 caracteres")
    private String nombreCurso;

    @Getter
    @Setter
    @Column
    private boolean estado;

    public Curso() {
    }
}
