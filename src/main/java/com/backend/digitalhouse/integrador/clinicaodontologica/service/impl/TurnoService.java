package com.backend.digitalhouse.integrador.clinicaodontologica.service.impl;

import com.backend.digitalhouse.integrador.clinicaodontologica.dao.IDao;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.modificacion.TurnoModificacionEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.turno.TurnoEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.salida.odontologo.OdontologoSalidaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.salida.paciente.PacienteSalidaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.salida.turno.TurnoSalidaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Paciente;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Turno;
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
    private final IDao<Turno> turnoIDao;
    private final ModelMapper modelMapper;
    private final OdontologoService odontologoService;
    private final PacienteService pacienteService;

    @Autowired
    public TurnoService(IDao<Turno> turnoIDao, ModelMapper modelMapper, OdontologoService odontologoService, PacienteService pacienteService) {
        this.turnoIDao = turnoIDao;
        this.modelMapper = modelMapper;
        this.odontologoService = odontologoService;
        this.pacienteService = pacienteService;
        configureMappings();
    }

    @Override
    public TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoEntradaDto) {
        TurnoSalidaDto turnoSalidaDto;

        PacienteSalidaDto paciente = pacienteService.buscarPacientePorId(turnoEntradaDto.getPacienteId());
        OdontologoSalidaDto odontologo = odontologoService.buscarOdontologoPorId(turnoEntradaDto.getOdontologoId());

        String pacienteNoEnBdd = "El paciente no se encuentra en nuestra base de datos";
        String odontologoNoEnBdd = "El odontologo no se encuentra en nuestra base de datos";

        if(paciente == null || odontologo == null){
            if(paciente == null && odontologo == null){
                LOGGER.error("El paciente y el odontologo no se encuentran en nuestra base de datos");
                throw new RuntimeException("El paciente y el odontologo no se encuentran en nuestra base de datos");
            } else if (paciente == null) {
                LOGGER.error(pacienteNoEnBdd);
                throw new RuntimeException(pacienteNoEnBdd);
            } else {
                LOGGER.error(odontologoNoEnBdd);
                throw new RuntimeException(odontologoNoEnBdd);
            }
        } else {
            Turno turnoNuevo = turnoIDao.registrar(modelMapper.map(turnoEntradaDto, Turno.class));
            turnoSalidaDto = entidadADtoSalida(turnoNuevo);
            LOGGER.info("Nuevo turno registrado con exito: {}", turnoSalidaDto);
        }

        return turnoSalidaDto;
    }

    @Override
    public List<TurnoSalidaDto> listarTurno() {
        List<Turno> turnos = turnoIDao.listarTodos();
        return turnos.stream()
                .map(this::entidadADtoSalida)
                .toList();
    }

    @Override
    public TurnoSalidaDto buscarTurnoPorId(int id) {
        return entidadADtoSalida(turnoIDao.buscarPorId(id));
    }

    @Override
    public void eliminarTurno(int id) {
        if (turnoIDao.buscarPorId(id) != null) turnoIDao.eliminar(id);
        else throw new RuntimeException("El turno no existe");
    }

    @Override
    public TurnoSalidaDto modificarTurno(TurnoModificacionEntradaDto turnoModificado) {
        TurnoSalidaDto turnoSalidaDto = null;
        Turno turnoAModificar = turnoIDao.buscarPorId(turnoModificado.getId());

        if(turnoAModificar != null){
            turnoAModificar = dtoModificadoAEntidad(turnoModificado);
            turnoSalidaDto = entidadADtoSalida(turnoIDao.modificar(turnoAModificar));
        }
        return turnoSalidaDto;
    }

    private void configureMappings() {
        modelMapper.typeMap(Turno.class, TurnoSalidaDto.class)
                .addMappings(mapper -> mapper.map(Turno::getPaciente, TurnoSalidaDto::setPacienteTurnoSalidaDto))
                .addMappings(mapper -> mapper.map(Turno::getOdontologo, TurnoSalidaDto::setOdontologoTurnoSalidaDto));
    }

    public TurnoSalidaDto entidadADtoSalida(Turno tueno) {
        return modelMapper.map(tueno, TurnoSalidaDto.class);
    }

    public Turno dtoEntradaAEntidad(TurnoEntradaDto turnoEntradaDto) {
        return modelMapper.map(turnoEntradaDto, Turno.class);
    }

    public Turno dtoModificadoAEntidad(TurnoModificacionEntradaDto turnoEntradaDto) {
        return modelMapper.map(turnoEntradaDto, Turno.class);
    }
}
