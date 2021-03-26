package com.example.demo.Dao;

import com.example.demo.Model.TipoDocumento;
import org.springframework.data.repository.CrudRepository;

public interface IDaoTipoDocumento extends CrudRepository<TipoDocumento, Long> {
}
