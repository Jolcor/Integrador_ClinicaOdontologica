package com.backend.digitalhouse.integrador.clinicaodontologica.service;

import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.turno.TurnoEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.salida.turno.TurnoSalidaDto;

import java.util.List;

public interface ITurnoService {
    TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoEntradaDto);
    List<TurnoSalidaDto> listarTurno();
    TurnoSalidaDto buscarTurnoPorId(int id);
    void eliminarTurno(int id);

}
