package com.backend.integrador.service.impl;


import com.backend.integrador.dto.odontologo.OdontologoEntradaDTO;
import com.backend.integrador.dto.odontologo.OdontologoSalidaDTO;
import com.backend.integrador.entity.Odontologo;
import com.backend.integrador.exception.ResourceNotFoundException;
import com.backend.integrador.repository.OdontologoRepository;
import com.backend.integrador.service.IOdontologoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OdontologoServiceImpl implements IOdontologoService {
    private final Logger LOGGER = LoggerFactory.getLogger(OdontologoServiceImpl.class);

    private OdontologoRepository odontologoRepository;
    private ModelMapper modelMapper;


    public OdontologoServiceImpl(OdontologoRepository odontologoRepository, ModelMapper modelMapper) {
        this.odontologoRepository = odontologoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OdontologoSalidaDTO guardarOdontologo(OdontologoEntradaDTO odontologo) {
        Odontologo odontologoGuardado = odontologoRepository.save(modelMapper.map(odontologo, Odontologo.class));
        LOGGER.info("Odontologo guardado: {}", odontologoGuardado);
        return modelMapper.map(odontologoGuardado, OdontologoSalidaDTO.class);
    }

    @Override
    public List<OdontologoSalidaDTO> buscarTodosLosOdontologos() {
        return odontologoRepository.findAll()
                .stream()
                .map(odontologo -> modelMapper.map(odontologo, OdontologoSalidaDTO.class))
                .toList();
    }

    @Override
    public OdontologoSalidaDTO buscarOdontologoPorId(Long id) {
        return odontologoRepository.findById(id).map(odontologo -> modelMapper.map(odontologo, OdontologoSalidaDTO.class))
                .orElseGet(() -> {
                    LOGGER.info("No se encontró el odontologo con id: {}", id);
                    return null;
                });
    }

    @Override
    public void eliminarOdontologo(Long id) throws ResourceNotFoundException {
        if (!odontologoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontró el odontologo a eliminar con id: " + id);
        }
        odontologoRepository.deleteById(id);
        LOGGER.info("Odontologo eliminado con id: {}", id);
    }


    @Override
    public OdontologoSalidaDTO actualizarOdontologo(OdontologoEntradaDTO odontologo, Long id) throws ResourceNotFoundException {
        Optional<Odontologo> odontologoAActualizar = odontologoRepository.findById(id);
        if (odontologoAActualizar.isPresent()) {
            Odontologo odontologoActualizado = odontologoRepository.save(modelMapper.map(Odontologo.builder()
                    .id(id)
                    .apellido(odontologo.getApellido())
                    .nombre(odontologo.getNombre())
                    .numeroDeMatricula(odontologo.getNumeroDeMatricula())
                    .build(), Odontologo.class));
            LOGGER.info("Odontologo actualizado: {}", odontologoActualizado);
            return modelMapper.map(odontologoActualizado, OdontologoSalidaDTO.class);
        } else {
            throw new ResourceNotFoundException("No se encontró el odontologo a actualizar con id: " + id);
        }
    }


}
