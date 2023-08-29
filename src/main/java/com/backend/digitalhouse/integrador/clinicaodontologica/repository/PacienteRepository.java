package com.backend.digitalhouse.integrador.clinicaodontologica.repository;

import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
