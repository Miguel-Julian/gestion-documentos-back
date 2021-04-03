package com.example.demo.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name="Calificacion")
public class Calificacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "id_calificacion") //Si el campo tiene un nombre d  istinto en la BD
    private long idCalificacion;

    @Getter
    @Setter
    @Column(name = "nombre_calificacion")
    @Size(min=3, max=30, message = "El nombre de la calificacion debe esar entre 3 y 30 caracteres")
    private String nombreCalificacion;

    @Getter
    @Setter
    @Column(name = "descripcion_calificacion")
    @Size(min=3, max=255, message = "La descripcion de la calificacion debe esar entre 3 y 255 caracteres")
    private String descripcionCalificacion;

    @Getter
    @Setter
    @Column(name = "nota_minima")
    private float notaMinima;

    @Getter
    @Setter
    @Column(name = "nota_Maxima")
    private float notaMaxima;

    @Getter
    @Setter
    @Column(name = "nota_aprobacion")
    private float notaAprobacion;



    @Getter
    @Setter
    @Column
    private boolean isSelect;


    @Getter
    @Setter
    @Column
    private boolean estado;

    public Calificacion() {
    }
}