package com.backend.digitalhouse.integrador.clinicaodontologica.dao.impl;

import com.backend.digitalhouse.integrador.clinicaodontologica.dao.H2Connection;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.IDao;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Odontologo;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Paciente;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Turno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class TurnoDao implements IDao<Turno> {
    private final Logger LOGGER = LoggerFactory.getLogger(PacienteDaoH2.class);

    @Override
    public Turno registrar(Turno turno) {
        Connection connection = null;
        Turno turnoPrimero = null;
        try {
            // Establecer la conexión a la base de datos y deshabilitar el autocommit
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            TurnoDao turnoDao = new TurnoDao();

            // Intentar registrar el turno en la base de datos
            turnoPrimero = turnoDao.registrar(turno);

            Odontologo odontologo = turno.getOdontologo();
            Paciente paciente = turno.getPaciente();

            // Preparar la sentencia SQL para insertar un nuevo turno
            PreparedStatement ps = connection.prepareStatement("INSERT INTO TURNO (ODONTOLOGO, PACIENTE, FECHAYHORA) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            if (odontologo == null || paciente == null) {
                throw new RuntimeException("El turno no se puede generar");
            } else {

                // Establecer los valores de los parámetros de la sentencia SQL
                ps.setInt(1, odontologo.getId());
                ps.setInt(2, paciente.getId());
                ps.setObject(3, turno.getFechaYHora());

                // Ejecutar la sentencia SQL y obtener las claves generadas
                turnoPrimero = new Turno(turno.getOdontologo(), turno.getPaciente(), turno.getFechaYHora());
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();

                while (rs.next()) {
                    paciente.setId(rs.getInt(1)); // Actualizar el ID del turno con el valor generado
                }

                connection.commit();
                LOGGER.info("Se ha registrado el turno: " + turnoPrimero);
            }
        } catch (Exception e) {
            // Manejar las excepciones, realizar rollback y cerrar la conexión
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException exception) {
                    LOGGER.error(exception.getMessage());
                    exception.printStackTrace();
                }
            }
        } finally {
            // Cerrar la conexión a la base de datos
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                LOGGER.error("Ha ocurrido un error al intentar cerrar la base de datos. " + e.getMessage());
                e.printStackTrace();
            }
        }
        return turnoPrimero;
    }


    @Override
    public Turno buscarPorId(int id) {
        Connection connection = null;
        Turno turno = null;

        try {
            connection = H2Connection.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM TURNOS WHERE ID = ?", id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) turno = crearObjetoTurno(rs);
        }
        catch (Exception e){
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception ex){
                LOGGER.error("Ha ocurrido un error al intentar cerrar la bdd. {}", ex.getMessage());
                ex.printStackTrace();
            }
        }
        return turno;
    }

    @Override
    public void eliminar(int id) {

    }

    @Override
    public List<Turno> listarTodos() {
        return null;
    }

    @Override
    public Turno modificar(Turno turno) {
        return null;
    }

    private Turno crearObjetoTurno(ResultSet resultSet) throws SQLException {
        int idTurno = resultSet.getInt("id");
        int idOdontologo = resultSet.getInt("id_odontologo");
        int idPaciente = resultSet.getInt("id_paciente");
        LocalDateTime fechaYHora = resultSet.getTimestamp("fecha").toLocalDateTime();

        TurnoDao turnoDao = new TurnoDao();
        Turno turnoPaciente = turnoDao.buscarPorId(resultSet.getInt("turno_id"));

        Odontologo odontologo = new Odontologo();
        odontologo.setId(idOdontologo);

        Paciente paciente = new Paciente();
        paciente.setId(idPaciente);

        return new Turno(idTurno, odontologo, paciente, fechaYHora, turnoPaciente);
    }


}
