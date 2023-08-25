package com.backend.digitalhouse.integrador.clinicaodontologica.dao.impl;

import com.backend.digitalhouse.integrador.clinicaodontologica.dao.H2Connection;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.IDao;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Domicilio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DomicilioDaoH2 implements IDao<Domicilio> {
    private final Logger LOGGER = LoggerFactory.getLogger(DomicilioDaoH2.class);
    @Override
    public Domicilio registrar(Domicilio domicilio) {

        Connection connection = null;
        Domicilio domicilio1 = null;

        try {

            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement ps = connection.prepareStatement("INSERT INTO DOMICILIOS (CALLE, NUMERO, LOCALIDAD, PROVINCIA) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, domicilio.getCalle());
            ps.setInt(2, domicilio.getNumero());
            ps.setString(3, domicilio.getLocalidad());
            ps.setString(4, domicilio.getProvincia());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            domicilio1 = new Domicilio(domicilio.getCalle(), domicilio.getNumero(), domicilio.getLocalidad(), domicilio.getProvincia());

            while (rs.next()){
                domicilio1.setId(rs.getInt("id"));
            }

            connection.commit();
            LOGGER.info("Se ha registrado el domicilio: {}", domicilio1);

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
            } catch (Exception ex) {
                LOGGER.error("Ha ocurrido un error al intentar cerrar la bdd. {}", ex.getMessage());
                ex.printStackTrace();
            }
        }
        return domicilio1;
    }

    @Override
    public Domicilio buscarPorId(int id) {
        Domicilio domicilio = null;
        Connection connection = null;
        try {
            connection = H2Connection.getConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM DOMICILIOS WHERE ID = ?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                domicilio = new Domicilio(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getString(5));
            }

            if(domicilio == null) LOGGER.error("No se ha encontrado el domicilio con id: {}", id);
            else LOGGER.info("Se ha encontrado el domicilio: " + domicilio);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                LOGGER.error("Ha ocurrido un error al intentar cerrar la bdd. {}", ex.getMessage());
                ex.printStackTrace();
            }
        }
        return domicilio;
    }

    @Override
    public void eliminar(int id) {

        Connection connection = null;

        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("DELETE FROM DOMICILIOS WHERE ID = ?", id);
            //ps.setInt(1, id);
            ps.execute();

            connection.commit();
            LOGGER.warn("Se ha eliminado el domicilio con id: {}", id);

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
            } catch (Exception ex) {
                LOGGER.error("Ha ocurrido un error al intentar cerrar la bdd. {}", ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    @Override
    public List<Domicilio> listarTodos() {
        Connection connection = null;
        List<Domicilio> domicilios = new ArrayList<>();

        try{
            connection = H2Connection.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM DOMICILIOS");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Domicilio domicilio = new Domicilio(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getString(5));
                domicilios.add(domicilio);
            }
            LOGGER.info("Listado de domicilios obtenido: {}", domicilios);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();

        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                LOGGER.error("Ha ocurrido un error al intentar cerrar la bdd. {}", ex.getMessage());
                ex.printStackTrace();
            }
        }
        return  domicilios;
    }

    @Override
    public Domicilio modificar(Domicilio domicilioModificado) {
        Connection connection = null;
        try {
            connection = H2Connection.getConnection();

            PreparedStatement ps = connection.prepareStatement("UPDATE DOMICILIO SET CALLE = ?, NUMERO = ?, LOCALIDAD = ?, PROVINCIA = ? WHERE ID = ?");
            ps.setString(1, domicilioModificado.getCalle());
            ps.setInt(2, domicilioModificado.getNumero());
            ps.setString(3, domicilioModificado.getLocalidad());
            ps.setString(4, domicilioModificado.getProvincia());
            ps.execute();

            LOGGER.warn("El paciente con id {} ha sido modificado: {}", domicilioModificado.getId(), domicilioModificado);

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
        return domicilioModificado;
    }
}