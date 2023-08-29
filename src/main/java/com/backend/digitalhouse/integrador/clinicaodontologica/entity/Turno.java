package com.backend.digitalhouse.integrador.clinicaodontologica.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TURNOS")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ODONTOLOGO_ID", nullable = false, length = 50)
    private Odontologo odontologo;

    @Column(name = "PACIENTE_ID", nullable = false, length = 50)
    private Paciente paciente;

    @Column(name = "FECHA_Y_HORA", nullable = false)
    private LocalDateTime fechaYHora;

    public Turno() {
    }

    public Turno(Odontologo odontologo, Paciente paciente, LocalDateTime fechaYHora) {
        this.odontologo = odontologo;
        this.paciente = paciente;
        this.fechaYHora = fechaYHora;
    }

    public Long getId() {
            return id;
        }

    public Odontologo getOdontologo() {
        return odontologo;
    }

    public void setOdontologo(Odontologo odontologo) {
        this.odontologo = odontologo;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDateTime getFechaYHora() {
        return fechaYHora;
    }

    public void setFechaYHora(LocalDateTime fechaYHora) {
        this.fechaYHora = fechaYHora;
    }
}