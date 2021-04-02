package com.example.demo.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "Temas")
public class Tema implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "id_tema")
    private long idTema;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    private AsignacionDocente asignacionDocente;

    @Getter
    @Setter
    @Column(name = "nombre_tema")
    @Size(min=3, max=50, message = "El nombre de la materia debe estar entre 3 y 50 caracteres")
    private String nombreTema;

    @Getter
    @Setter
    @Column
    private boolean estado;

    public Tema() {
    }
}
