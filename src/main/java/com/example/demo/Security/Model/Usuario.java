package com.example.demo.Security.Model;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;

@Entity
@Table(name = "Usuarios")

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "id_usuario")
    private long idUsuario;

    @Getter
    @Setter
    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    @Getter
    @Setter
    @Column
    private  String contrasenia;

    @Getter
    @Setter
    @Column
    private String rol;

    @Getter
    @Setter
    @Column
    private boolean estado;


    public Usuario() {
    }
}
