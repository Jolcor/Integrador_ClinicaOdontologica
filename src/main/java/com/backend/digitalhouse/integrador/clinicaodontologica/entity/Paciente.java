package com.backend.digitalhouse.integrador.clinicaodontologica.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PACIENTES")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NOMBRE_PACIENTE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PACIENTE", nullable = false, length = 50)
    private String apellido;

    @Column(name = "DNI", nullable = false, length = 50)
    private int dni;
    //@JsonProperty("fechaIngreso") en caso de que el campo a mapear este escrito distinto a nuestro modelo

    @Column(name = "FECHA_Y_HORA", nullable = false)
    private LocalDateTime fechaIngreso;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DOMICILIO_ID", referencedColumnName = "id") // DOMICILIO_ID -> nombre de la clave foranea
    private Domicilio domicilio;

    @OneToMany(mappedBy = "paciente")// nombre de la columna que tiene relación
    @JsonIgnore
    private Set<Turno> turnos = new HashSet<>();

    public Paciente() {
    }

    public Paciente(String nombre, String apellido, int dni, LocalDateTime fechaIngreso, Domicilio domicilio) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaIngreso = fechaIngreso;
        this.domicilio = domicilio;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    @Override
    public String toString() {
        return "Id: " + id + " - Nombre: " + nombre + " - Apellido: " + apellido + " - DNI: " + dni + " - Fechas de ingreso: " + fechaIngreso + " - Domicilio: " + domicilio;
    }
}
