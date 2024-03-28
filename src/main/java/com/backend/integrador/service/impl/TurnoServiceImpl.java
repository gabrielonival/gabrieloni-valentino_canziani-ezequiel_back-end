package com.backend.integrador.service.impl;

import com.backend.integrador.dto.odontologo.OdontologoSalidaDTO;
import com.backend.integrador.dto.paciente.PacienteSalidaDTO;
import com.backend.integrador.dto.turno.TurnoEntradaDTO;
import com.backend.integrador.dto.turno.TurnoSalidaDTO;
import com.backend.integrador.entity.Odontologo;
import com.backend.integrador.entity.Paciente;
import com.backend.integrador.entity.Turno;
import com.backend.integrador.exception.BadRequestException;
import com.backend.integrador.exception.ResourceNotFoundException;
import com.backend.integrador.repository.TurnoRepository;
import com.backend.integrador.service.IOdontologoService;
import com.backend.integrador.service.IPacienteService;
import com.backend.integrador.service.ITurnoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurnoServiceImpl implements ITurnoService {
    private final Logger LOGGER = LoggerFactory.getLogger(TurnoServiceImpl.class);
    private ModelMapper modelMapper;
    private TurnoRepository turnoRepository;
    private IPacienteService pacienteService;
    private IOdontologoService odontologoService;

    public TurnoServiceImpl(IPacienteService pacienteService, IOdontologoService odontologoService, TurnoRepository turnoRepository, ModelMapper modelMapper) {
        this.pacienteService = pacienteService;
        this.odontologoService = odontologoService;
        this.modelMapper = modelMapper;
        this.turnoRepository = turnoRepository;
    }

    @Override
    public TurnoSalidaDTO registrarTurno(TurnoEntradaDTO turnoEntradaDTO) throws BadRequestException {

        OdontologoSalidaDTO odontologo = odontologoService.buscarOdontologoPorId(turnoEntradaDTO.getOdontologoId());
        PacienteSalidaDTO paciente = pacienteService.buscarPacientePorId(turnoEntradaDTO.getPacienteId());

        validarOdontologoYPaciente(odontologo, paciente);

        LOGGER.info("Registrando turno para el paciente: {} y el odontologo: {}", paciente, odontologo);

        Odontologo odontologoEntity = modelMapper.map(odontologo, Odontologo.class);
        Paciente pacienteEntity = modelMapper.map(paciente, Paciente.class);

        Turno turnoGuardado = turnoRepository.save(Turno.builder()
                .odontologo(odontologoEntity)
                .paciente(pacienteEntity)
                .fechaYHora(turnoEntradaDTO.getFechaYHora())
                .build());

        LOGGER.info("Turno guardado: {}", turnoGuardado);

        return modelMapper.map(turnoGuardado, TurnoSalidaDTO.class);
    }

    @Override
    public List<TurnoSalidaDTO> listarTurnos() {
        return turnoRepository.findAll()
                .stream()
                .map(turno -> modelMapper.map(turno, TurnoSalidaDTO.class))
                .toList();
    }

    @Override
    public TurnoSalidaDTO buscarTurnoPorId(Long id) {
        return turnoRepository.findById(id).map(turno -> modelMapper.map(turno, TurnoSalidaDTO.class))
                .orElseGet(() -> {
                    LOGGER.info("No se encontró el turno con id: {}", id);
                    return null;
                });
    }

    @Override
    public void eliminarTurno(Long id) throws ResourceNotFoundException {
        if (!turnoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontró el turno para eliminar con el id: " + id);
        }
        turnoRepository.deleteById(id);
        LOGGER.info("turno eliminado con id: {}", id);


    }

    @Override
    public TurnoSalidaDTO actualizarTurno(TurnoEntradaDTO turnoEntradaDTO, Long id) throws ResourceNotFoundException {
        return turnoRepository.findById(id)
                .map(turno -> {
                    OdontologoSalidaDTO odontologo = odontologoService.buscarOdontologoPorId(turnoEntradaDTO.getOdontologoId());
                    PacienteSalidaDTO paciente = pacienteService.buscarPacientePorId(turnoEntradaDTO.getPacienteId());
                    return modelMapper.map(turnoRepository.save(Turno.builder()
                            .id(turno.getId())
                            .odontologo(modelMapper.map(odontologo, Odontologo.class))
                            .paciente(modelMapper.map(paciente, Paciente.class))
                            .fechaYHora(turnoEntradaDTO.getFechaYHora())
                            .build()), TurnoSalidaDTO.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el turno a actualizar con id: " + id));
    }

    private void validarOdontologoYPaciente(OdontologoSalidaDTO odontologo, PacienteSalidaDTO paciente) throws BadRequestException {
        if (odontologo == null || paciente == null) {
            if (odontologo == null && paciente == null) {
                throw new BadRequestException("El odontologo y el paciente no existen");
            } else if (odontologo == null) {
                throw new BadRequestException("El odontologo no existe");
            } else {
                throw new BadRequestException("El paciente no existe");
            }
        }
    }
}
