package com.backend.digitalhouse.integrador.clinicaodontologica.service.impl;

import com.backend.digitalhouse.integrador.clinicaodontologica.dto.entrada.modificacion.TurnoModificacionEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dto.entrada.turno.TurnoEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dto.salida.turno.TurnoSalidaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Odontologo;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Paciente;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Turno;
import com.backend.digitalhouse.integrador.clinicaodontologica.exeptions.BadRequestException;
import com.backend.digitalhouse.integrador.clinicaodontologica.exeptions.ResourceNotFoundException;
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
        configureMappings();
    }

    @Override
    public TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoEntradaDto) throws BadRequestException {

        Odontologo odontologo = odontologoRepository.getReferenceById(turnoEntradaDto.getOdontologoId());
        Paciente paciente = pacienteRepository.getReferenceById(turnoEntradaDto.getOdontologoId());
        Turno turnoNuevo = null;

        if(paciente != null && odontologo != null) {
            turnoNuevo = turnoRepository.save(dtoEntradaAEntidad(turnoEntradaDto));
            LOGGER.info("Nuevo turno registrado con exito: {}", turnoNuevo);
        } else
            LOGGER.error("El paciente o el odontologo no se encuentran en bdd..");
            throw new BadRequestException("El paciente o el odontologo no se encuentran en bdd..");
    }

    @Override
    public List<TurnoSalidaDto> listarTurno() {
        List<TurnoSalidaDto> turnos = turnoRepository.findAll().stream()
                .map(this::entidadADtoSalida)
                .toList();
        LOGGER.info("Listado de turnos: {}", turnos);
        return turnos;
    }

    @Override
    public TurnoSalidaDto buscarTurnoPorId(Long id) {
        Turno turnoBuscado = turnoRepository.findById(id).orElse(null);
        TurnoSalidaDto turnoSalida = null;
        if(turnoBuscado != null) {
            turnoSalida = entidadADtoSalida(turnoBuscado);
            LOGGER.info("Turno por id: {}", turnoSalida);
        } else {
            LOGGER.error("El de id_turno no se encuentra registrador.. ");
        }
        return turnoSalida;
    }

    @Override
    public void eliminarTurno(Long id) throws ResourceNotFoundException {
        if (buscarTurnoPorId(id) != null) {
            turnoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado turno con id: {}", id);
        } else {
            LOGGER.error("No se ha encontrado el turno con id {}", id);
            throw new ResourceNotFoundException("No se ha encontrado el odontologo con id " + id);
        }
    }

    @Override
    public TurnoSalidaDto modificarTurno(TurnoModificacionEntradaDto turnoModificado) {
        Turno turnoRecibido = dtoModificadoAEntidad(turnoModificado);
        Turno turnoAModificar = turnoRepository.findById(turnoRecibido.getId()).orElse(null);
        TurnoSalidaDto turnoSalidaDto = null;

        if (turnoAModificar != null) {

            turnoAModificar = turnoRepository.save(turnoAModificar);
            turnoSalidaDto = entidadADtoSalida(turnoAModificar);
            LOGGER.warn("Turno actualizar: {}", turnoSalidaDto);

        } else LOGGER.error("No ses posible actualizar los datos, el turno no se encuentra registrado");
        return turnoSalidaDto;
    }

    private void configureMappings() {
        modelMapper.typeMap(Turno.class, TurnoSalidaDto.class)
                .addMappings(mapper -> mapper.map(Turno::getPaciente, TurnoSalidaDto::setPacienteTurnoSalidaDto))
                .addMappings(mapper -> mapper.map(Turno::getOdontologo, TurnoSalidaDto::setOdontologoTurnoSalidaDto));
    }

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
