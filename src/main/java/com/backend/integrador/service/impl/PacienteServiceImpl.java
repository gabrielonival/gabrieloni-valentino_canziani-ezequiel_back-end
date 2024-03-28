package com.backend.integrador.service.impl;

import com.backend.integrador.dto.paciente.PacienteEntradaDTO;
import com.backend.integrador.dto.paciente.PacienteSalidaDTO;
import com.backend.integrador.entity.Domicilio;
import com.backend.integrador.entity.Paciente;
import com.backend.integrador.exception.ResourceNotFoundException;
import com.backend.integrador.repository.PacienteRepository;
import com.backend.integrador.service.IPacienteService;
import com.backend.integrador.utils.JsonPrinter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteServiceImpl implements IPacienteService {

    private final Logger LOGGER = LoggerFactory.getLogger(PacienteServiceImpl.class);

    private PacienteRepository pacienteRepository;

    private ModelMapper modelMapper;

    public PacienteServiceImpl(PacienteRepository pacienteRepository, ModelMapper modelMapper) {
        this.pacienteRepository = pacienteRepository;
        this.modelMapper = modelMapper;
        configureMapping();
    }

    @Override
    public PacienteSalidaDTO guardarPaciente(PacienteEntradaDTO paciente) {
        LOGGER.info("Guardando paciente: {}", JsonPrinter.toString(paciente));
        Paciente pacienteGuardado = pacienteRepository.save(modelMapper.map(paciente, Paciente.class));
        LOGGER.info("Paciente guardado: {}", JsonPrinter.toString(pacienteGuardado));
        return modelMapper.map(pacienteGuardado, PacienteSalidaDTO.class);
    }

    @Override
    public List<PacienteSalidaDTO> listarPacientes() {
        return pacienteRepository.findAll()
                .stream()
                .map(paciente -> modelMapper.map(paciente, PacienteSalidaDTO.class))
                .toList();
    }

    @Override
    public PacienteSalidaDTO buscarPacientePorId(Long id) {
        return pacienteRepository.findById(id).map(paciente -> modelMapper.map(paciente, PacienteSalidaDTO.class
        )).orElseGet(() -> {
            LOGGER.info("No se encontró el paciente con id: {}", id);
            return null;
        });
    }

    @Override
    public PacienteSalidaDTO actualizarPaciente(PacienteEntradaDTO paciente, Long id) throws ResourceNotFoundException {
        Optional<Paciente> pacienteAModificar = pacienteRepository.findById(id);
        if (pacienteAModificar.isPresent()) {
            Paciente pacienteModificado = pacienteRepository.save(modelMapper.map(crearPacienteAModificar(paciente,pacienteAModificar.get()), Paciente.class));
            LOGGER.info("Paciente modificado: {}", pacienteModificado);
            return modelMapper.map(pacienteModificado, PacienteSalidaDTO.class);
        } else {
            throw new ResourceNotFoundException("No se encontró el paciente a actualizar con id: " + id);
        }
    }

    @Override
    public void eliminarPaciente(Long id) throws ResourceNotFoundException {
        if (!pacienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontró el paciente a eliminar con id: " + id);
        }
        pacienteRepository.deleteById(id);
        LOGGER.info("Paciente eliminado con id: {}", id);

    }

    private void configureMapping() {
        modelMapper.typeMap(PacienteEntradaDTO.class, Paciente.class)
                .addMappings(mapper -> mapper.map(PacienteEntradaDTO::getDomicilioEntradaDTO, Paciente::setDomicilio));


        modelMapper.typeMap(Paciente.class, PacienteSalidaDTO.class)
                .addMappings(mapper -> mapper.map(Paciente::getDomicilio, PacienteSalidaDTO::setDomicilioSalidaDTO));

    }

    private Paciente crearPacienteAModificar(PacienteEntradaDTO pacienteEntradaDTO, Paciente pacienteExistente) {
        return Paciente.builder()
                .id(pacienteExistente.getId())
                .nombre(pacienteEntradaDTO.getNombre())
                .apellido(pacienteEntradaDTO.getApellido())
                .dni(pacienteEntradaDTO.getDni())
                .fechaIngreso(pacienteEntradaDTO.getFechaIngreso())
                .domicilio(Domicilio.builder()
                        .id(pacienteExistente.getDomicilio().getId())
                        .calle(pacienteEntradaDTO.getDomicilioEntradaDTO().getCalle())
                        .numero(pacienteEntradaDTO.getDomicilioEntradaDTO().getNumero())
                        .localidad(pacienteEntradaDTO.getDomicilioEntradaDTO().getLocalidad())
                        .provincia(pacienteEntradaDTO.getDomicilioEntradaDTO().getProvincia())
                        .build())
                .build();
    }
}
