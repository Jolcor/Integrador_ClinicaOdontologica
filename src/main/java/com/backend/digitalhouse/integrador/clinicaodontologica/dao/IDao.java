package com.backend.digitalhouse.integrador.clinicaodontologica.dao;


import java.util.List;

public interface IDao<T>{
    //registrar, buscar todos
    T registrar(T t);
    List<T> listarTodos();
}
