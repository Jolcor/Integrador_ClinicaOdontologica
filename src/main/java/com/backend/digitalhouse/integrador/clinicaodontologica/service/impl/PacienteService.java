package com.backend.digitalhouse.integrador.clinicaodontologica.service.impl;


import com.backend.digitalhouse.integrador.clinicaodontologica.dao.IDao;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.paciente.PacienteEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Paciente;
import com.backend.digitalhouse.integrador.clinicaodontologica.service.IPacienteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PacienteService implements IPacienteService{
    private final IDao<Paciente> pacienteIDao;
    private final ModelMapper modelMapper;

    @Autowired
    public PacienteService(IDao<Paciente> pacienteIDao, ModelMapper modelMapper) {
        this.pacienteIDao = pacienteIDao;
        this.modelMapper = modelMapper;
        configureMapping();
    }

    // Método para registrar un paciente
    public Paciente registrarPaciente(PacienteEntradaDto paciente){
        //convertir Dto de entrada a entidad para poder enviarlo a la capa de persistencia
        // Convertir el DTO a entidad utilizando ModelMapper
        Paciente pacienteAPersistir = mapToEntity(paciente);
        // Registrar el paciente en la capa de persistencia
        Paciente pacienteRegistrado = pacienteIDao.registrar(pacienteAPersistir);

        return pacienteRegistrado;
    }
    @Override
    public Paciente buscarPacientePorId(int id){
        return pacienteIDao.buscarPorId(id);
    }
    @Override
    public List<Paciente> listarPacientes(){
        return pacienteIDao.listarTodos();
    }
    @Override
    public void eliminarPaciente(int id){
        pacienteIDao.eliminar(id);
    }

    @Override
    public Paciente modificarPaciente(Paciente pacienteModificado) {
        return pacienteIDao.modificar(pacienteModificado);
    }

    // Método para configurar reglas de mapeo personalizado en ModelMapper

    private void configureMapping(){
        modelMapper.typeMap(PacienteEntradaDto.class, Paciente.class)
                .addMappings(mapper -> mapper.map(PacienteEntradaDto::getDomicilio, Paciente::setDomicilio));
                // Método para convertir el DTO a entidad utilizando ModelMapper
    }

    // Método para convertir el DTO a entidad utilizando ModelMapper

    public Paciente mapToEntity(PacienteEntradaDto pacienteEntradaDto){
        return modelMapper.map(pacienteEntradaDto, Paciente.class);
    }


}