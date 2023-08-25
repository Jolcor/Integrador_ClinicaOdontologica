package com.backend.digitalhouse.integrador.clinicaodontologica.service.impl;

import com.backend.digitalhouse.integrador.clinicaodontologica.dao.IDao;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.modificacion.OdontologoModificacionEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.entrada.odontologo.OdontologoEntradaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.dao.dto.salida.odontologo.OdontologoSalidaDto;
import com.backend.digitalhouse.integrador.clinicaodontologica.entity.Odontologo;
import com.backend.digitalhouse.integrador.clinicaodontologica.service.IOdontologoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OdontologoService implements IOdontologoService {
    private final IDao<Odontologo> odontologoIDao;
    private final ModelMapper modelMapper;

    @Autowired
    public OdontologoService(IDao<Odontologo> odontologoIDao, ModelMapper modelMapper) {
        this.odontologoIDao = odontologoIDao;
        this.modelMapper = modelMapper;
        configureMapping();

    }
    @Override
    public OdontologoSalidaDto registrarOdontologo(OdontologoEntradaDto odontologo) {
        Odontologo odontologoRecibido = dtoEntradaAEntidad(odontologo);
        Odontologo odontologoRegistrado = odontologoIDao.registrar(odontologoRecibido);

        return entidadADtoSalida(odontologoRegistrado);
    }

    public OdontologoSalidaDto buscarOdontologoPorId(int id) {
        return entidadADtoSalida(odontologoIDao.buscarPorId(id));
    }

    public List<OdontologoSalidaDto> listarOdontologos() {
        return odontologoIDao.listarTodos().stream()
                .map(this::entidadADtoSalida)
                .toList();
    }

    public void eliminarOdontologo(int id) {
        if(odontologoIDao.buscarPorId(id) != null) odontologoIDao.eliminar(id);
        else new RuntimeException("El odontologo no existe");
    }

    @Override
    public OdontologoSalidaDto actualizarOdontologo(OdontologoModificacionEntradaDto odontologoModificado) {
        OdontologoSalidaDto odontologoSalidaDto = null;
        Odontologo odontologoAModificar = odontologoIDao.buscarPorId(odontologoModificado.getId());

        if(odontologoAModificar != null){
            odontologoAModificar = dtoModificadoAEntidad(odontologoModificado);
            odontologoSalidaDto = entidadADtoSalida(odontologoIDao.modificar(odontologoAModificar));
        }
        return odontologoSalidaDto;
    }

    private void configureMapping() {
        modelMapper.typeMap(OdontologoEntradaDto.class, Odontologo.class)
                .addMappings(mapper -> mapper.map(OdontologoEntradaDto::getMatricula, Odontologo::setMatricula));
        modelMapper.typeMap(Odontologo.class, OdontologoSalidaDto.class)
                .addMappings(mapper -> mapper.map(Odontologo::getMatricula, OdontologoSalidaDto::setMatricula));
        modelMapper.typeMap(OdontologoModificacionEntradaDto.class, Odontologo.class)
                .addMappings(mapper -> mapper.map(OdontologoModificacionEntradaDto::getMatricula, Odontologo::setMatricula));
    }

    public Odontologo dtoEntradaAEntidad(OdontologoEntradaDto odontologoEntradaDto) {
        return modelMapper.map(odontologoEntradaDto, Odontologo.class);
    }

    public OdontologoSalidaDto entidadADtoSalida(Odontologo odontologo) {
        return modelMapper.map(odontologo, OdontologoSalidaDto.class);
    }

    public Odontologo dtoModificadoAEntidad(OdontologoModificacionEntradaDto odontologoEntradaDto) {
        return modelMapper.map(odontologoEntradaDto, Odontologo.class);
    }
}



