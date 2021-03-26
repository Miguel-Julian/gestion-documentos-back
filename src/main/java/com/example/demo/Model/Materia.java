package com.example.demo.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "Materias")

public class Materia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "id_materia")
    private long idMateria;

    @Getter
    @Setter
    @Column(name = "nombre_materia")
    @Size(min=5, max=30, message = "El nombre de la materia debe estar entre 5 y 30 caracteres")
    private String nombreMateria;

    @Getter
    @Setter
    @Column
    private boolean estado;

    @Getter
    @Setter
    @Column
    private String descripcion;
}
