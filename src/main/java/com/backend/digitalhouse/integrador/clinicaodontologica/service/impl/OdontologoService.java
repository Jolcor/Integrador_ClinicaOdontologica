package com.backend.digitalhouse.integrador.clinicaodontologica.service.impl;


import com.backend.digitalhouse.integrador.clinicaodontologica.dao.IDao;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Odontologo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OdontologoService {
        private static final Logger LOGGER = LoggerFactory.getLogger(OdontologoService.class);

        private IDao<Odontologo> odontologoIDao;

        public OdontologoService(IDao<Odontologo> odontologoIDao) {
            this.odontologoIDao = odontologoIDao;
        }

        public Odontologo registrarOdontologo(Odontologo odontologo){
            return odontologoIDao.registrar(odontologo);
        }

        public List<Odontologo> listarOdontologos(){
            LOGGER.info("Listando los odontologos");
            return odontologoIDao.listarTodos();
        }

}



