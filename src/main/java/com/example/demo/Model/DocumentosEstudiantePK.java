package com.example.demo.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable

public class DocumentosEstudiantePK implements Serializable {
    @Getter
    @Setter
    private long idEstudiante;

    @Getter
    @Setter
    private long idDocumentosDocente;
}
