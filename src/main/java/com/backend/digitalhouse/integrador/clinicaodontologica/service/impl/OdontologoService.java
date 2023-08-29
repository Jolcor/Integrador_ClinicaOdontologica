package com.backend.digitalhouse.integrador.clinicaodontologica.service.impl;

import com.backend.digitalhouse.integrador.clinicaodontologica.dao.IDao;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.modificacion.OdontologoModificacionEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.odontologo.OdontologoEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.salida.odontologo.OdontologoSalidaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Odontologo;
import com.backend.digitalhouse.integrador.clinicaodontologica.repository.OdontologoRepository;
import com.backend.digitalhouse.integrador.clinicaodontologica.service.IOdontologoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OdontologoService implements IOdontologoService {
    private final Logger LOGGER = LoggerFactory.getLogger(PacienteService.class);
    private final OdontologoRepository odontologoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OdontologoService(OdontologoRepository odontologoRepository, ModelMapper modelMapper) {
        this.odontologoRepository = odontologoRepository;
        this.modelMapper = modelMapper;

    }
    @Override
    public OdontologoSalidaDto registrarOdontologo(OdontologoEntradaDto odontologo) {
        Odontologo odontologoNuevo = odontologoRepository.save(dtoEntradaAEntidad(odontologo));
        LOGGER.info("Odontologo ha sido registrado: {}", odontologoNuevo);
        return entidadADtoSalida(odontologoNuevo);
    }

    public OdontologoSalidaDto buscarOdontologoPorId(Long id) {
        LOGGER.info("Odontolgo por id: {}", id);

        return entidadADtoSalida(odontologoRepository.getById(id));
    }

    public List<OdontologoSalidaDto> listarOdontologos() {
        List<Odontologo> odontologos = odontologoRepository.findAll();
        LOGGER.info("Lista de odontologos...");

        return odontologos.stream()
                .map(this::entidadADtoSalida)
                .toList();
    }

    public void eliminarOdontologo(Long id) {
        if(odontologoRepository != null) odontologoRepository.deleteById(id);
        new RuntimeException("El odontologo no existe..");
    }

    @Override
    public OdontologoSalidaDto actualizarOdontologo(OdontologoModificacionEntradaDto odontologoModificado) {
        Odontologo odontologoActualizado = odontologoRepository.save(dtoModificadoAEntidad(odontologoModificado));
        return entidadADtoSalida(odontologoActualizado);
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



