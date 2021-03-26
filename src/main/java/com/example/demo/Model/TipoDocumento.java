package com.example.demo.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tipo_documento")

public class TipoDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "idTipo_documento")
    private long idTipoDocumento;

    @Getter
    @Setter
    @Size(min = 3, max = 50, message = "El nombre del tipo de documento debe esstar entre 3 y 50 caracteres")
    @Column(name = "nombre")
    private String nombreTipoDocumento;
}
