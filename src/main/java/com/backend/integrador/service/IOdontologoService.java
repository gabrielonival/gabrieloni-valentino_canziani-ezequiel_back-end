package com.backend.integrador.service;

import com.backend.integrador.dto.odontologo.OdontologoEntradaDTO;
import com.backend.integrador.dto.odontologo.OdontologoSalidaDTO;
import com.backend.integrador.entity.Odontologo;
import com.backend.integrador.exception.ResourceNotFoundException;

import java.util.List;

public interface IOdontologoService {

    OdontologoSalidaDTO guardarOdontologo(OdontologoEntradaDTO odontologo);
    List<OdontologoSalidaDTO> buscarTodosLosOdontologos();
    OdontologoSalidaDTO buscarOdontologoPorId(Long id);
    void eliminarOdontologo(Long id) throws ResourceNotFoundException;

    OdontologoSalidaDTO actualizarOdontologo(OdontologoEntradaDTO odontologo,Long id) throws ResourceNotFoundException;
}
