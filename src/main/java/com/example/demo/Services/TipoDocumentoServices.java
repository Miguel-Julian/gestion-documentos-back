package com.example.demo.Services;

import com.example.demo.Dao.IDaoTipoDocumento;
import com.example.demo.Model.TipoDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class TipoDocumentoServices {
    @Autowired
    IDaoTipoDocumento iDaoTipoDocumento;

    public void registrarTipoDoc (TipoDocumento tipoDocumento){
        iDaoTipoDocumento.save(tipoDocumento);
    }

    public List<TipoDocumento> listar(){
        return (List<TipoDocumento>) iDaoTipoDocumento.findAll();
    }

    public TipoDocumento consultarTipoDoc(long idTipoDocumento){
        return iDaoTipoDocumento.findById(idTipoDocumento).orElse(null);
    }

}
