package com.example.demo.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable

public class AsignacionPK implements Serializable {
    @Getter
    @Setter
    private long idCurso;

    @Getter
    @Setter
    private long idMateria;
}
