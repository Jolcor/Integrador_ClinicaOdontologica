package com.backend.digitalhouse.integrador.clinicaodontologica.repository;

import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    /*@Query("select t from TURNOS t join t.paciente p where p.apellido = ?1")
    List<Turno> listarTurnoPorApellidoPaciente(String apellido);*/

}
