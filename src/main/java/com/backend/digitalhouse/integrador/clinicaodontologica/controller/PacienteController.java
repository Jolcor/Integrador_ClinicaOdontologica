package com.backend.digitalhouse.integrador.clinicaodontologica.controller;

import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.paciente.PacienteEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Paciente;
import com.backend.digitalhouse.integrador.clinicaodontologica.service.IPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//http://localhost:8080/
@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final IPacienteService pacienteService;

    @Autowired
    public PacienteController(IPacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    //POST
    @PostMapping("registrar")
    public Paciente registrarPaciente(@RequestBody PacienteEntradaDto pacienteRegistrado){
        return pacienteService.registrarPaciente(pacienteRegistrado);
    }

    //PUT
    @PutMapping("modificar")
    public Paciente modificarPaciente(@RequestBody Paciente pacienteModificado){
        return pacienteService.modificarPaciente(pacienteModificado);
    }

    //DELETE
    @DeleteMapping("pacientes/eliminar/{id}")
    public ResponseEntity<String> eliminarUnPaciente(@PathVariable int id) {
        ResponseEntity<String> response = null;

        if (pacienteService.buscarPacientePorId(id) != null) {
            pacienteService.eliminarPaciente(id);
            response = ResponseEntity.status(HttpStatus.NO_CONTENT).body("Eliminado");
        } else {
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return response;
    }
    //GET
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> listarTodosLosPacientes(@PathVariable int id) {
        Paciente paciente = pacienteService.buscarPacientePorId(id);
        return ResponseEntity.ok(paciente);
    }

}
