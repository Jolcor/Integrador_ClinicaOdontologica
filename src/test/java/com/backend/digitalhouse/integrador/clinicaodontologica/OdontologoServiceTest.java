package com.backend.digitalhouse.integrador.clinicaodontologica;

import com.backend.digitalhouse.integrador.clinicaodontologica.dto.entrada.modificacion.OdontologoModificacionEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dto.entrada.odontologo.OdontologoEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.exceptions.ResourceNotFoundException;
import com.backend.digitalhouse.integrador.clinicaodontologica.service.impl.OdontologoService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ClinicaOdontologicaApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OdontologoServiceTest {
    @Autowired
    private OdontologoService odontologoService;

    @Test
    @Order(1)
    void alIntentarAgregarUnOdontologoLlamadoJorge_deberaRegistarlo() {

        try {

            OdontologoEntradaDto odontologoNuevo = new OdontologoEntradaDto("121321", "Jorge", "Corrales");
            odontologoService.registrarOdontologo(odontologoNuevo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(odontologoService.buscarOdontologoPorId(1L));
        assertEquals("Jorge", odontologoService.buscarOdontologoPorId(1L).getNombre());

    }

    @Test
    @Order(2)
    void alIntentarEliminarElOdontologo_deberaEliminarExitosamente() {

        try {
            OdontologoEntradaDto odontologoEntradaDto = new OdontologoEntradaDto("121321", "Jorge", "Corrales");
            odontologoService.registrarOdontologo(odontologoEntradaDto);
            odontologoService.eliminarOdontologo(1L);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNull(odontologoService.buscarOdontologoPorId(1L));
    }

    @Test
    @Order(3)
    void alIntentarEliminarUnOdontoloqueNoExita_deberaLanzardeberaUnaResourceNotFoundException() {

        OdontologoModificacionEntradaDto odontologoModificacionEntradaDto = new OdontologoModificacionEntradaDto();
        odontologoModificacionEntradaDto.setId(2L);

        assertThrows(ResourceNotFoundException.class, () -> odontologoService.actualizarOdontologo(odontologoModificacionEntradaDto));

    }
}
