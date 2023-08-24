package com.backend.digitalhouse.integrador.clinicaodontologica.dao.impl;

import com.backend.digitalhouse.integrador.clinicaodontologica.dao.IDao;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Turno;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TurnoDao implements IDao<Turno> {
    @Override
    public Turno registrar(Turno turno) {
        return null;
    }

    @Override
    public Turno buscarPorId(int id) {
        return null;
    }

    @Override
    public void eliminar(int id) {

    }

    @Override
    public List<Turno> listarTodos() {
        return null;
    }

    @Override
    public Turno modificar(Turno turno) {
        return null;
    }
}
