package com.backend.digitalhouse.integrador.clinicaodontologica.dao.impl;


import com.backend.digitalhouse.integrador.clinicaodontologica.dao.H2Connection;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.IDao;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Domicilio;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Odontologo;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Paciente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PacienteDaoH2 implements IDao<Paciente> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacienteDaoH2.class);
    private DomicilioDaoH2 domicilioDaoH2 = new DomicilioDaoH2();

    @Override
    public Paciente registrar(Paciente paciente) {
        Connection connection = null;
        Paciente paciente1 = null;
        try {
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            DomicilioDaoH2 domicilioDaoH2 = new DomicilioDaoH2();
            Domicilio domicilio = domicilioDaoH2.registrar(paciente.getDomicilio());

            PreparedStatement ps = connection.prepareStatement("INSERT INTO PACIENTES (NOMBRE, APELLIDO, DNI, FECHA, DOMICILIO_ID) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, paciente.getNombre());
            ps.setString(2, paciente.getApellido());
            ps.setInt(3, paciente.getDni());
            ps.setDate(4, Date.valueOf(paciente.getFechaIngreso()));
            ps.setInt(5, paciente.getDomicilio().getId());
            ps.execute();

            paciente1 = new Paciente(paciente.getNombre(), paciente.getApellido(), paciente.getDni(), paciente.getFechaIngreso(), domicilio);

            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                paciente.setId(rs.getInt(1));
            }

            connection.commit();
            if (paciente1 == null) LOGGER.error("No fue posible registrar al paciente");
            else LOGGER.info("Se ha registrado el paciente: " + paciente1);


        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                    System.out.println("Tuvimos un problema");
                    e.printStackTrace();
                } catch (SQLException exception) {
                    LOGGER.error(exception.getMessage());
                    exception.printStackTrace();
                }
            }
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                LOGGER.error("Ha ocurrido un error al intentar cerrar la bd. " + e.getMessage());
                e.printStackTrace();
            }
        }

        return paciente1;
    }
    @Override
    public List<Paciente> listarTodos() {
        Connection connection = null;
        List<Paciente> pacientes = new ArrayList<>();

        try{
            connection = H2Connection.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM PACIENTES");
            Domicilio domicilio = domicilioDaoH2.registrar(Paciente.getDomicilio());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Paciente paciente = new Paciente(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getDate(4).toLocalDate(), domicilio);
                pacientes.add(paciente);
            }

            if(pacientes.isEmpty()) {
                LOGGER.error("No se ha encontraron pacientes");
            } else {
                LOGGER.info("Se encontraron los pacientes: " + pacientes);
            }
        } catch (Exception e){
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception ex){
                LOGGER.error("Ha ocurrido un error al intentar cerrar la bd. " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return pacientes;
    }
}