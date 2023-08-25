package com.backend.digitalhouse.integrador.clinicaodontologica.dao;

import java.util.List;

public interface IDao<T>{
    T registrar(T t);
    T buscarPorId(int id);
    void eliminar(int id);
    List<T> listarTodos();
    T modificar(T t);
}
