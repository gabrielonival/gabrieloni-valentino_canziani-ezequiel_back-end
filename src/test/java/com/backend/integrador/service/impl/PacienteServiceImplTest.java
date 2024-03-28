package com.backend.integrador.service.impl;

import com.backend.integrador.dto.domicilio.DomicilioEntradaDTO;
import com.backend.integrador.dto.paciente.PacienteEntradaDTO;
import com.backend.integrador.dto.paciente.PacienteSalidaDTO;
import com.backend.integrador.entity.Domicilio;
import com.backend.integrador.entity.Paciente;
import com.backend.integrador.exception.ResourceNotFoundException;
import com.backend.integrador.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class PacienteServiceImplTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    private PacienteServiceImpl pacienteService;

    private PacienteEntradaDTO pacienteEntradaDTO;

    private Paciente paciente;

    @BeforeEach
    void setUp() {
        pacienteEntradaDTO = PacienteEntradaDTO.builder()
                .nombre("nicolas")
                .apellido("sanchez")
                .dni(12345)
                .domicilioEntradaDTO(
                        DomicilioEntradaDTO.builder()
                                .calle("calle")
                                .localidad("localidad")
                                .numero(1234)
                                .provincia("provincia")
                                .build())
                .build();

        paciente = Paciente.builder()
                .id(1L)
                .nombre("nicolas")
                .apellido("sanchez")
                .dni(12345)
                .fechaIngreso(LocalDate.of(2027, 12, 27))
                .domicilio(Domicilio.builder()
                        .id(1L)
                        .calle("Avenida Libertador")
                        .numero(123)
                        .provincia("Canelones")
                        .localidad("Pando city")
                        .build())
                .build();
    }



    @Test
    void deberiaGuardarUnPaciente_YRetornarUnPacienteConSuId() {
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);
        PacienteSalidaDTO pacienteGuardado = pacienteService.guardarPaciente(pacienteEntradaDTO);

        verify(pacienteRepository, times(1)).save(any(Paciente.class));

        assertNotNull(pacienteGuardado.getId());
    }


    @Test
    void deberiaBuscarUnPacientePorId_YRetornarElPacienteEncontrado() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        PacienteSalidaDTO pacienteBuscado = pacienteService.buscarPacientePorId(1L);

        verify(pacienteRepository, times(1)).findById(1L);

        assertNotNull(pacienteBuscado);
        assertEquals(paciente.getId(), pacienteBuscado.getId());
    }

    @Test
    void deberiaBuscarUnPacientePorId_YRetornarNull() {
        when(pacienteRepository.findById(anyLong())).thenReturn(Optional.empty());
        PacienteSalidaDTO pacienteBuscado = pacienteService.buscarPacientePorId(1L);

        verify(pacienteRepository, times(1)).findById(1L);

        assertNull(pacienteBuscado);
    }

    @Test
    void deberiaBuscarUnaListaDePacientes_YRetornarUnaListaConElementos() {
        when(pacienteRepository.findAll()).thenReturn(List.of(paciente));
        List<PacienteSalidaDTO> pacientes = pacienteService.listarPacientes();

        verify(pacienteRepository, times(1)).findAll();

        assertFalse(pacientes.isEmpty());
        assertEquals(1, pacientes.size());
        assertEquals(paciente.getId(), pacientes.get(0).getId());
    }

    @Test
    void deberiaBuscarUnaListaDePacientes_YRetornarUnaListaVacia() {
        when(pacienteRepository.findAll()).thenReturn(List.of());
        List<PacienteSalidaDTO> pacientes = pacienteService.listarPacientes();

        verify(pacienteRepository, times(1)).findAll();

        assertTrue(pacientes.isEmpty());
    }

    @Test
    void deberiaIntentarEliminarUnPacienteInexistente_YLanzarExcepcionConMensaje() {
        when(pacienteRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> {
            pacienteService.eliminarPaciente(1L);
        });

        verify(pacienteRepository, times(1)).existsById(1L);
        verify(pacienteRepository, never()).deleteById(anyLong());

        assertEquals("No se encontró el paciente a eliminar con id: 1", resourceNotFoundException.getMessage());
    }

    @Test
    void deberiaEliminarUnPaciente_YNoRetornarNada() throws ResourceNotFoundException {
        when(pacienteRepository.existsById(1L)).thenReturn(true);
        pacienteService.eliminarPaciente(1L);

        verify(pacienteRepository, times(1)).existsById(1L);
        verify(pacienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void deberiaActualizarUnPacienteExistente_YRetornarElPacienteActualizado() throws ResourceNotFoundException {
        PacienteEntradaDTO pacienteEntradaActualizar =  PacienteEntradaDTO.builder()
                .nombre("nuevoNombre")
                .apellido("nuevoApellido")
                .dni(12322)
                .domicilioEntradaDTO(
                        DomicilioEntradaDTO.builder()
                                .calle("nuevaCalle")
                                .localidad("nuevaLocalidad")
                                .numero(12345)
                                .provincia("nuevaProvincia")
                                .build()).build();


        Paciente crearPacienteActualizado =  Paciente.builder()
                .id(1L)
                .nombre("nuevoNombre")
                .apellido("nuevoApellido")
                .dni(12322)
                .fechaIngreso(LocalDate.of(2027,12,27))
                .domicilio(Domicilio.builder()
                        .id(1L)
                        .calle("nuevaCalle")
                        .numero(12345)
                        .provincia("nuevaProvincia")
                        .localidad("nuevaLocalidad")
                        .build())
                .build();

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(crearPacienteActualizado);

        PacienteSalidaDTO pacienteActualizado = pacienteService.actualizarPaciente(pacienteEntradaActualizar,1L);

        verify(pacienteRepository, times(1)).save(any(Paciente.class));
        verify(pacienteRepository,times(1)).findById(1L);

        assertNotNull(pacienteActualizado);
        assertEquals(paciente.getId(),pacienteActualizado.getId());
        assertEquals(paciente.getDomicilio().getId(),pacienteActualizado.getDomicilioSalidaDTO().getId());
        assertNotEquals(paciente.getNombre(),pacienteActualizado.getNombre());
        assertNotEquals(paciente.getApellido(),pacienteActualizado.getApellido());
        assertNotEquals(paciente.getDni(),pacienteActualizado.getDni());
        assertNotEquals(paciente.getDomicilio().getCalle(),pacienteActualizado.getDomicilioSalidaDTO().getCalle());
        assertNotEquals(paciente.getDomicilio().getNumero(),pacienteActualizado.getDomicilioSalidaDTO().getNumero());
        assertNotEquals(paciente.getDomicilio().getProvincia(),pacienteActualizado.getDomicilioSalidaDTO().getProvincia());
        assertNotEquals(paciente.getDomicilio().getLocalidad(),pacienteActualizado.getDomicilioSalidaDTO().getLocalidad());
    }

    @Test
    void deberiaIntentarActualizarUnPacienteInexistente_YLanzarExcepcionConMensaje() {
        PacienteEntradaDTO pacienteEntradaActualizar = pacienteEntradaDTO;

        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> {
            pacienteService.actualizarPaciente(pacienteEntradaActualizar, 1L);
        });

        verify(pacienteRepository, times(1)).findById(1L);
        verify(pacienteRepository, never()).save(any(Paciente.class));

        assertEquals("No se encontró el paciente a actualizar con id: 1", resourceNotFoundException.getMessage());
    }

}