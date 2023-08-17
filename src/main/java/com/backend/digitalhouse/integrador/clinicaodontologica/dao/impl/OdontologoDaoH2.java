package com.backend.digitalhouse.integrador.clinicaodontologica.dao.impl;

import com.backend.digitalhouse.integrador.clinicaodontologica.dao.H2Connection;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.IDao;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Odontologo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OdontologoDaoH2 implements IDao<Odontologo> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OdontologoDaoH2.class);

    @Override
    public Odontologo registrar(Odontologo odontologo) {
        Connection connection = null;
        Odontologo odontologo1 = null;
        try {
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement ps = connection.prepareStatement("INSERT INTO ODONTOLOGOS (MATRICULA, NOMBRE, APELLIDO) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, odontologo.getMatricula());
            ps.setString(2, odontologo.getNombre());
            ps.setString(3, odontologo.getApellido());
            ps.execute();

            odontologo1 = new Odontologo(odontologo.getMatricula(), odontologo.getNombre(), odontologo.getApellido());

            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()){
                odontologo1.setId(rs.getInt(1));
            }

            connection.commit();
            if (odontologo1.getId() == 0) {
                LOGGER.error("No fue posible registrar al Odontologo");
            } else {
                LOGGER.info("Se ha registrado el Odontologo: " + odontologo1);
            }

        } catch (Exception e){
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            if(connection != null){
                try {
                    connection.rollback();
                    LOGGER.info("Tuvimos un problema");
                    e.printStackTrace();
                } catch (SQLException exception) {
                    LOGGER.error(exception.getMessage());
                    exception.printStackTrace();
                }
            }
        }finally {
            try {
                connection.close();
                LOGGER.info("Se cerro la conexion satisfactoriamente");
            } catch (Exception e){
                LOGGER.error("Ha ocurrido un error al intentar cerrar la bdd. " + e.getMessage());
                e.printStackTrace();
            }
        }

            return odontologo1;
        }


    @Override
    public List<Odontologo> listarTodos() {
        Connection connection = null;
        List<Odontologo> odontologos = new ArrayList<>();

        try{
            connection = H2Connection.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ODONTOLOGOS");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Odontologo odontologo = crearObjetoOdontologo(rs);
                odontologos.add(odontologo);
            }

            if(odontologos.isEmpty()) {
                LOGGER.error("No se ha encontraron odontologos");
            } else {
                LOGGER.info("Se encontraron los odontologos: " + odontologos);
            }
        } catch (Exception e){
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception ex){
                LOGGER.error("Ha ocurrido un error al intentar cerrar la bdd. " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return odontologos;
    }
    private Odontologo crearObjetoOdontologo(ResultSet resultSet) throws SQLException {
        int idOdontologo = resultSet.getInt("id");
        int matricula = resultSet.getInt("matricula");
        String nombre= resultSet.getString("nombre");
        String apellido = resultSet.getString("apellido");

        return new Odontologo(idOdontologo, matricula, nombre, apellido);
    }
}
