package com.backend.integrador.service;

import com.backend.integrador.dto.turno.TurnoEntradaDTO;
import com.backend.integrador.dto.turno.TurnoSalidaDTO;
import com.backend.integrador.exception.BadRequestException;
import com.backend.integrador.exception.ResourceNotFoundException;

import java.util.List;

public interface ITurnoService {

    TurnoSalidaDTO registrarTurno(TurnoEntradaDTO turnoEntradaDTO) throws BadRequestException;
    List<TurnoSalidaDTO> listarTurnos();

    TurnoSalidaDTO buscarTurnoPorId(Long id);

    void eliminarTurno(Long id) throws ResourceNotFoundException;

    TurnoSalidaDTO actualizarTurno(TurnoEntradaDTO turnoEntradaDTO,Long id) throws ResourceNotFoundException;
}
