package com.backend.digitalhouse.integrador.clinicaodontologica;

import com.backend.digitalhouse.integrador.clinicaodontologica.dto.entrada.odontologo.OdontologoEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dto.entrada.paciente.DomicilioEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dto.entrada.paciente.PacienteEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dto.entrada.turno.TurnoEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dto.salida.odontologo.OdontologoSalidaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dto.salida.paciente.PacienteSalidaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dto.salida.turno.OdontologoTurnoSalidaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dto.salida.turno.PacienteTurnoSalidaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dto.salida.turno.TurnoSalidaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.exceptions.BadRequestException;
import com.backend.digitalhouse.integrador.clinicaodontologica.exceptions.ResourceNotFoundException;
import com.backend.digitalhouse.integrador.clinicaodontologica.service.impl.OdontologoService;
import com.backend.digitalhouse.integrador.clinicaodontologica.service.impl.PacienteService;
import com.backend.digitalhouse.integrador.clinicaodontologica.service.impl.TurnoService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.NamedStoredProcedureQueries;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ClinicaOdontologicaApplication.class)
public class TurnoServiceTest {

    @Autowired
    private TurnoService turnoService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private OdontologoService odontologoService;


    @Test
    @Order(1)
    void alIntentarRegistrarUnTurnoSinIdPacienteYIdOdontoloho_deberiaLanzarUnaResourceNotFoundException() {

        TurnoEntradaDto turnoEntradaDto = new TurnoEntradaDto();
        assertThrows(BadRequestException.class, () -> {
            turnoService.registrarTurno(turnoEntradaDto);
        });
    }

    @Test
    @Order(2)
    void alListarTurnosAgregandoUnTurnoPreviamente_deberaRetornarUnaListaConUnTurno() {

        try {

            DomicilioEntradaDto domicilioPacienteNuevo = new DomicilioEntradaDto("Belen", 85, "Medellin", "Antioquia");
            PacienteEntradaDto pacienteNuevo = new PacienteEntradaDto("Luis", "Corrales", 123456, LocalDate.of(2023, 9, 11), domicilioPacienteNuevo);
            pacienteService.registrarPaciente(pacienteNuevo);
            PacienteSalidaDto paciente = pacienteService.buscarPacientePorId(1L);

            OdontologoEntradaDto odontologoNuevo = new OdontologoEntradaDto("121321", "Jorge", "Corrales");
            odontologoService.registrarOdontologo(odontologoNuevo);
            OdontologoSalidaDto odontologo = odontologoService.buscarOdontologoPorId(1L);

            LocalDateTime fechaTurno = LocalDateTime.of(2023, 9, 11, 12, 00);

            TurnoEntradaDto turnoNuevo = new TurnoEntradaDto(odontologo.getId(), paciente.getId(), fechaTurno);

            turnoService.registrarTurno(turnoNuevo);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(turnoService.buscarTurnoPorId(1L));
        assertTrue(turnoService.listarTurno().size() > 0);
    }


    @Test
    @Order(3)
    void alIntentarListarLosTurnosSinTenerTurno_deberaLanzarResourceNotFoundException() {

        try {

            DomicilioEntradaDto domicilioPacienteNuevo = new DomicilioEntradaDto("Belen", 85, "Medellin", "Antioquia");
            PacienteEntradaDto pacienteNuevo = new PacienteEntradaDto("Luis", "Corrales", 123456, LocalDate.of(2023, 9, 11), domicilioPacienteNuevo);
            pacienteService.registrarPaciente(pacienteNuevo);

            OdontologoEntradaDto odontologoNuevo = new OdontologoEntradaDto("121321", "Jorge", "Corrales");
            odontologoService.registrarOdontologo(odontologoNuevo);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNull(turnoService.buscarTurnoPorId(3L));
    }

}