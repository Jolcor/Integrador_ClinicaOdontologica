package com.backend.digitalhouse.integrador.clinicaodontologica.service;

import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.paciente.PacienteEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Paciente;

import java.util.List;

public interface IPacienteService {
    List<Paciente> listarPacientes();

    Paciente registrarPaciente(PacienteEntradaDto paciente);

    Paciente buscarPacientePorId(int id);

    void eliminarPaciente(int id);

    Paciente modificarPaciente(Paciente pacienteModificado);



}
