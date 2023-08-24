package com.backend.digitalhouse.integrador.clinicaodontologica.service.impl;


import com.backend.digitalhouse.integrador.clinicaodontologica.dao.IDao;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.modificacion.OdontologoModificacionEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.modificacion.PacienteModificacionEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.odontologo.OdontologoEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.salida.odontologo.OdontologoSalidaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Odontologo;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Paciente;
import com.backend.digitalhouse.integrador.clinicaodontologica.service.IOdontologoService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OdontologoService implements IOdontologoService {
    private final IDao<Odontologo> odontologoIDao;
    private final ModelMapper modelMapper;

    public OdontologoService(IDao<Odontologo> odontologoIDao, ModelMapper modelMapper) {
        this.odontologoIDao = odontologoIDao;
        this.modelMapper = modelMapper;
    }
    @Override
    public OdontologoSalidaDto registrarOdontologo(OdontologoEntradaDto odontologo) {
        return modelMapper.map(odontologoIDao.registrar(modelMapper.map(odontologo, Odontologo.class)), OdontologoSalidaDto.class);
    }

    public OdontologoSalidaDto buscarOdontologoPorId(int id) {
        return modelMapper.map(odontologoIDao.buscarPorId(id), OdontologoSalidaDto.class);
    }

    public List<OdontologoSalidaDto> listarOdontologos() {
        return odontologoIDao.listarTodos().stream().map(o -> modelMapper.map(o, OdontologoSalidaDto.class)).toList();
    }

    public void eliminarOdontologo(int id) {
        odontologoIDao.eliminar(id);
    }

    private void configureMapping() {
        modelMapper.typeMap(OdontologoEntradaDto.class, Odontologo.class)
                .addMappings(mapper -> mapper.map(OdontologoEntradaDto::getMatricula, Odontologo::setMatricula));
        modelMapper.typeMap(Odontologo.class, OdontologoSalidaDto.class)
                .addMappings(mapper -> mapper.map(Odontologo::getMatricula, OdontologoSalidaDto::setMatricula));
        modelMapper.typeMap(PacienteModificacionEntradaDto.class, Paciente.class)
                .addMappings(mapper -> mapper.map(PacienteModificacionEntradaDto::getDomicilio, Paciente::setDomicilio));

    }

    public Odontologo dtoEntradaAEntidad(OdontologoEntradaDto odontologoEntradaDto) {
        return modelMapper.map(odontologoEntradaDto, Odontologo.class);
    }

    public OdontologoSalidaDto entidadADtoSalida(Odontologo odontologo) {
        return modelMapper.map(odontologo, OdontologoSalidaDto.class);
    }

    public Odontologo dtoModificadoAEntidad(OdontologoModificacionEntradaDto odontologoEntradaDto) {
        return modelMapper.map(odontologoEntradaDto, Odontologo.class);
    }
}



