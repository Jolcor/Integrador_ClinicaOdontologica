package com.backend.digitalhouse.integrador.clinicaodontologica;

import com.backend.digitalhouse.integrador.clinicaodontologica.dto.entrada.modificacion.PacienteModificacionEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dto.entrada.paciente.DomicilioEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dto.entrada.paciente.PacienteEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.exceptions.ResourceNotFoundException;
import com.backend.digitalhouse.integrador.clinicaodontologica.service.impl.PacienteService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = ClinicaOdontologicaApplication.class)
public class PacienteServiceTest {
    @Autowired
    private PacienteService pacienteService;

    @Test
    @Order(1)
    void deberiaAgregarUnPaciente_cuandoLasCasillasEstenCompletas() {
        try {

            DomicilioEntradaDto domicilioPacienteNuevo = new DomicilioEntradaDto("Belen", 85, "Medellin", "Antioquia");

            PacienteEntradaDto pacienteNuevo = new PacienteEntradaDto("Luis", "Corrales", 123456, LocalDate.of(2023, 9, 11), domicilioPacienteNuevo);

            pacienteService.registrarPaciente(pacienteNuevo);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(pacienteService.buscarPacientePorId(1L).getId());
        assertEquals("Luis", pacienteService.buscarPacientePorId(1L).getNombre());

    }

    @Test
    @Order(2)
    void deberiaListarTodosLosPacienteRegistrados_siempreQueLaBDDNoEsteBasia() {

        try {
            DomicilioEntradaDto domicilioPacienteNuevo = new DomicilioEntradaDto("Belen", 85, "Medellin", "Antioquia");

            PacienteEntradaDto pacienteNuevo = new PacienteEntradaDto("Luis", "Corrales", 123456, LocalDate.of(2023, 9, 11), domicilioPacienteNuevo);

            pacienteService.registrarPaciente(pacienteNuevo);
        } catch (Exception e){
            e.printStackTrace();
        }
        assertTrue(pacienteService.listarPacientes().size() > 0);

    }

    @Test
    @Order(3)
    void alIntertarActualizarElpacienteId2_deberaLanzarUnaResourceNotFoundException() {

        PacienteModificacionEntradaDto pacienteModificacionEntradaDto = new PacienteModificacionEntradaDto();
        pacienteModificacionEntradaDto.setId(2L);
        assertThrows(ResourceNotFoundException.class, () -> pacienteService.modificarPaciente(pacienteModificacionEntradaDto));

    }

    @Test
    @Order(4)
    void alIntentarBuscarElPacientePorId_deberaMostrarlo() {

        try {
            DomicilioEntradaDto domicilioPacienteNuevo = new DomicilioEntradaDto("Belen", 85, "Medellin", "Antioquia");

            PacienteEntradaDto pacienteNuevo = new PacienteEntradaDto("Luis", "Corrales", 123456, LocalDate.of(2023, 9, 11), domicilioPacienteNuevo);

            pacienteService.registrarPaciente(pacienteNuevo);

        } catch (Exception e){
            e.printStackTrace();
        }

        assertEquals(1, pacienteService.buscarPacientePorId(1L).getId());
    }

    @Test
    @Order(5)
    void alIntentarEliminarUnPacienteYaEliminado_deberaLanzarUnResourceNotFoundException() {
        try {
            pacienteService.eliminarPaciente(1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertThrows(ResourceNotFoundException.class, () -> pacienteService.eliminarPaciente(1L));
    }

}
