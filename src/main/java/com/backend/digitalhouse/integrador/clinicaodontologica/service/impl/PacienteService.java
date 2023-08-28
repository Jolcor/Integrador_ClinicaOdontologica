package com.backend.digitalhouse.integrador.clinicaodontologica.service.impl;

import com.backend.digitalhouse.integrador.clinicaodontologica.dao.IDao;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.modificacion.PacienteModificacionEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.paciente.PacienteEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.salida.paciente.PacienteSalidaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Paciente;
import com.backend.digitalhouse.integrador.clinicaodontologica.service.IPacienteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PacienteService implements IPacienteService {
    private final IDao<Paciente> pacienteIDao;
    private final ModelMapper modelMapper;
    @Autowired
    public PacienteService(IDao<Paciente> pacienteIDao, ModelMapper modelMapper) {
        this.pacienteIDao = pacienteIDao;
        this.modelMapper = modelMapper;
        configureMapping();
    }

    private void configureMapping() {
        // Mapeo de PacienteEntradaDto a Paciente
        modelMapper.createTypeMap(PacienteEntradaDto.class, Paciente.class)
                .addMapping(PacienteEntradaDto::getDomicilio, Paciente::setDomicilio);

        // Mapeo de PacienteModificacionEntradaDto a Paciente
        modelMapper.createTypeMap(PacienteModificacionEntradaDto.class, Paciente.class)
                .addMapping(PacienteModificacionEntradaDto::getDomicilio, Paciente::setDomicilio);

        // Mapeo de Paciente a PacienteSalidaDto
        modelMapper.createTypeMap(Paciente.class, PacienteSalidaDto.class)
                .addMapping(Paciente::getDomicilio, PacienteSalidaDto::setDomicilio);
    }


    public PacienteSalidaDto registrarPaciente(PacienteEntradaDto paciente) {
        Paciente pacienteRecibido = dtoEntradaAEntidad(paciente);
        Paciente pacienteRegistrado = pacienteIDao.registrar(pacienteRecibido);

        return entidadADtoSalida(pacienteRegistrado);
    }

    @Override
    public PacienteSalidaDto modificarPaciente(PacienteModificacionEntradaDto pacienteModificado) {
        PacienteSalidaDto pacienteSalidaDto = null;
        Paciente pacienteAModificar = pacienteIDao.buscarPorId(pacienteModificado.getId());

        if(pacienteAModificar != null){
            pacienteAModificar = dtoModificadoAEntidad(pacienteModificado);
            pacienteSalidaDto = entidadADtoSalida(pacienteIDao.modificar(pacienteAModificar));

        }

        return pacienteSalidaDto;
    }

    @Override
    public PacienteSalidaDto buscarPacientePorId(int id) {
        return entidadADtoSalida(pacienteIDao.buscarPorId(id));
    }

    @Override
    public List<PacienteSalidaDto> listarPacientes() {
        List<Paciente> pacientes = pacienteIDao.listarTodos();

        return pacientes.stream()
                .map(this::entidadADtoSalida)
                .toList();
    }

    @Override
    public void eliminarPaciente(int id) {
        pacienteIDao.eliminar(id);
    }

    public Paciente dtoEntradaAEntidad(PacienteEntradaDto pacienteEntradaDto) {
        return modelMapper.map(pacienteEntradaDto, Paciente.class);
    }

    public PacienteSalidaDto entidadADtoSalida(Paciente paciente) {
        return modelMapper.map(paciente, PacienteSalidaDto.class);
    }

    public Paciente dtoModificadoAEntidad(PacienteModificacionEntradaDto pacienteEntradaDto) {
        return modelMapper.map(pacienteEntradaDto, Paciente.class);
    }
}