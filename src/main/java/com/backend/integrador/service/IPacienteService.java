package com.backend.integrador.service;

import com.backend.integrador.dto.paciente.PacienteEntradaDTO;
import com.backend.integrador.dto.paciente.PacienteSalidaDTO;
import com.backend.integrador.exception.ResourceNotFoundException;

import java.util.List;

public interface IPacienteService {

    PacienteSalidaDTO guardarPaciente(PacienteEntradaDTO paciente);

    List<PacienteSalidaDTO> listarPacientes();

    PacienteSalidaDTO buscarPacientePorId(Long id);

    PacienteSalidaDTO actualizarPaciente(PacienteEntradaDTO paciente,Long id) throws ResourceNotFoundException;

    void eliminarPaciente(Long id) throws ResourceNotFoundException;
}
