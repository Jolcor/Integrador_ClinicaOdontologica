package com.backend.digitalhouse.integrador.clinicaodontologica.service;

import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.modificacion.OdontologoModificacionEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.odontologo.OdontologoEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.salida.odontologo.OdontologoSalidaDto;

import java.util.List;

public interface IOdontologoService {
    List<OdontologoSalidaDto> listarOdontologos();
    OdontologoSalidaDto registrarOdontologo(OdontologoEntradaDto odontologo);
    OdontologoSalidaDto buscarOdontologoPorId(int id);
    void eliminarOdontologo(int id);
    OdontologoSalidaDto actualizarOdontologo(OdontologoModificacionEntradaDto odontologoModificado);


}