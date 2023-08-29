package com.backend.digitalhouse.integrador.clinicaodontologica.service.impl;

import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.modificacion.PacienteModificacionEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.paciente.PacienteEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.salida.paciente.PacienteSalidaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Paciente;
import com.backend.digitalhouse.integrador.clinicaodontologica.repository.PacienteRepository;
import com.backend.digitalhouse.integrador.clinicaodontologica.service.IPacienteService;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PacienteService implements IPacienteService {
    private final Logger LOGGER = LoggerFactory.getLogger(PacienteService.class);

    private final PacienteRepository  pacienteRepository;
    private ModelMapper modelMapper;

    @Autowired
    public PacienteService(PacienteRepository pacienteRepository, ModelMapper modelMapper) {
        this.pacienteRepository = pacienteRepository;
        this.modelMapper = modelMapper;
        //configureMapping();
    }

    @Override
    public PacienteSalidaDto modificarPaciente(PacienteModificacionEntradaDto pacienteModificado) {
        Paciente pacienteActualizado = pacienteRepository.save(dtoModificadoAEntidad(pacienteModificado));
        LOGGER.info("EL paciente ha sido actualizado: {}", pacienteActualizado);

        return entidadADtoSalida(pacienteActualizado);
    }

    @Override
    public PacienteSalidaDto buscarPacientePorId(Long id) {
        LOGGER.info("Pacientes por id: {}", id);

        return entidadADtoSalida(pacienteRepository.getReferenceById(id));
    }

    @Override
    public List<PacienteSalidaDto> listarPacientes() {
        List<Paciente> pacientes = pacienteRepository.findAll();
        LOGGER.info("Lista de pacientes...");

        return pacientes.stream()
                .map(this::entidadADtoSalida)
                .toList();
    }

    @Override
    public PacienteSalidaDto registrarPaciente(PacienteEntradaDto paciente) {
        Paciente pacienteNuevo = pacienteRepository.save(dtoEntradaAEntidad(paciente));
        LOGGER.info("EL paciente ha sido registrado: {}", pacienteNuevo);
        return entidadADtoSalida(pacienteNuevo);
    }
    @Override
    public void eliminarPaciente(Long id) {
        if(pacienteRepository != null )pacienteRepository.deleteById(id);
        new RuntimeException("El paciente no existe..");
    }

    /*private void configureMapping() {
        modelMapper.typeMap(PacienteEntradaDto.class, Paciente.class)
                .addMappings(mapper -> mapper.map(PacienteEntradaDto::getDomicilio, Paciente::setDomicilio));
        modelMapper.typeMap(Paciente.class, PacienteSalidaDto.class)
                .addMappings(mapper -> mapper.map(Paciente::getDomicilio, PacienteSalidaDto::setDomicilio));
        modelMapper.typeMap(PacienteModificacionEntradaDto.class, Paciente.class)
                .addMappings(mapper -> mapper.map(PacienteModificacionEntradaDto::getDomicilio, Paciente::setDomicilio));
    }*/

    public Paciente dtoEntradaAEntidad(PacienteEntradaDto pacienteEntradaDto) {
        return modelMapper.map(pacienteEntradaDto, Paciente.class);
    }

    public PacienteSalidaDto entidadADtoSalida(Paciente paciente) {
        return modelMapper.map(paciente, PacienteSalidaDto.class);
    }

    public Paciente dtoModificadoAEntidad(PacienteModificacionEntradaDto pacienteModificacionEntradaDto) {
        return modelMapper.map(pacienteModificacionEntradaDto, Paciente.class);
    }

}