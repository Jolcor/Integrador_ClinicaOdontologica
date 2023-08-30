package com.backend.digitalhouse.integrador.clinicaodontologica.service.impl;

import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.modificacion.TurnoModificacionEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.turno.TurnoEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.salida.turno.TurnoSalidaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Odontologo;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Paciente;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Turno;
import com.backend.digitalhouse.integrador.clinicaodontologica.repository.OdontologoRepository;
import com.backend.digitalhouse.integrador.clinicaodontologica.repository.PacienteRepository;
import com.backend.digitalhouse.integrador.clinicaodontologica.repository.TurnoRepository;
import com.backend.digitalhouse.integrador.clinicaodontologica.service.ITurnoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurnoService implements ITurnoService {

    private final Logger LOGGER = LoggerFactory.getLogger(TurnoService.class);
    private final TurnoRepository turnoRepository;
    private final ModelMapper modelMapper;
    private final OdontologoRepository odontologoRepository;
    private final PacienteRepository pacienteRepository;

    @Autowired
    public TurnoService(TurnoRepository turnoRepository, ModelMapper modelMapper, OdontologoRepository odontologoRepository, PacienteRepository pacienteRepository) {
        this.turnoRepository = turnoRepository;
        this.modelMapper = modelMapper;
        this.odontologoRepository = odontologoRepository;
        this.pacienteRepository = pacienteRepository;
        //configureMappings();
    }

    @Override
    public TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoEntradaDto) {

        Odontologo odontologo = odontologoRepository.getReferenceById(turnoEntradaDto.getOdontologoId());
        Paciente paciente = pacienteRepository.getReferenceById(turnoEntradaDto.getOdontologoId());
        Turno turnoNuevo = null;

        if(paciente != null && odontologo != null) {
            turnoNuevo = turnoRepository.save(dtoEntradaAEntidad(turnoEntradaDto));
            LOGGER.info("Nuevo turno registrado con exito: {}", turnoNuevo);
        } else {
            LOGGER.info("El paciente o el odontologo no se encuentran en bdd..");
            throw new RuntimeException("El paciente o el odontologo no se encuentran en bdd..");
        }
        return entidadADtoSalida(turnoNuevo);
    }

    @Override
    public List<TurnoSalidaDto> listarTurno() {
        List<Turno> turnos = turnoRepository.findAll();
        return turnos.stream()
                .map(this::entidadADtoSalida)
                .toList();
    }

    @Override
    public TurnoSalidaDto buscarTurnoPorId(Long id) {
        return entidadADtoSalida(turnoRepository.getReferenceById(id));
    }

    @Override
    public void eliminarTurno(Long id) {
        if (turnoRepository.getReferenceById(id) != null) turnoRepository.deleteById(id);
        else throw new RuntimeException("El turno no existe");
    }

    @Override
    public TurnoSalidaDto modificarTurno(TurnoModificacionEntradaDto turnoModificado) {

        LOGGER.info("inicio del metodo put: actualizar turno");

        return null;
    }

    /*private void configureMappings() {
        modelMapper.typeMap(Turno.class, TurnoSalidaDto.class)
                .addMappings(mapper -> mapper.map(Turno::getPaciente, TurnoSalidaDto::setPacienteTurnoSalidaDto))
                .addMappings(mapper -> mapper.map(Turno::getOdontologo, TurnoSalidaDto::setOdontologoTurnoSalidaDto));
    }*/

    public TurnoSalidaDto entidadADtoSalida(Turno turno) {
        return modelMapper.map(turno, TurnoSalidaDto.class);
    }

    public Turno dtoEntradaAEntidad(TurnoEntradaDto turnoEntradaDto) {
        return modelMapper.map(turnoEntradaDto, Turno.class);
    }

    public Turno dtoModificadoAEntidad(TurnoModificacionEntradaDto turnoEntradaDto) {
        return modelMapper.map(turnoEntradaDto, Turno.class);
    }
}
