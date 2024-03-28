package com.backend.integrador.service.impl;

import com.backend.integrador.dto.domicilio.DomicilioSalidaDTO;
import com.backend.integrador.dto.odontologo.OdontologoSalidaDTO;
import com.backend.integrador.dto.paciente.PacienteSalidaDTO;
import com.backend.integrador.dto.turno.TurnoEntradaDTO;
import com.backend.integrador.dto.turno.TurnoSalidaDTO;
import com.backend.integrador.entity.Domicilio;
import com.backend.integrador.entity.Odontologo;
import com.backend.integrador.entity.Paciente;
import com.backend.integrador.entity.Turno;
import com.backend.integrador.exception.BadRequestException;
import com.backend.integrador.exception.ResourceNotFoundException;
import com.backend.integrador.repository.TurnoRepository;
import com.backend.integrador.service.IOdontologoService;
import com.backend.integrador.service.IPacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TurnoServiceImplTest {

    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private IPacienteService pacienteService;

    @Mock
    private IOdontologoService odontologoService;

    @Spy
    private ModelMapper modelMapper;

    private TurnoEntradaDTO turnoEntradaDTO;
    private OdontologoSalidaDTO odontologo;

    private PacienteSalidaDTO paciente;

    private Turno turno;

    @InjectMocks
    private TurnoServiceImpl turnoService;

    @BeforeEach
    void setUp() {

        paciente = PacienteSalidaDTO.builder()
                .id(1L)
                .nombre("Nicolas")
                .apellido("Sanchez")
                .dni(12345)
                .domicilioSalidaDTO(DomicilioSalidaDTO.builder()
                        .calle("Calle Falsa")
                        .numero(1234)
                        .localidad("Springfield")
                        .provincia("Springfield")
                        .build()).build();

        odontologo = OdontologoSalidaDTO.builder()
                .id(1L)
                .nombre("Nicolas")
                .apellido("Sanchez")
                .numeroDeMatricula("12345")
                .build();

        turno = Turno.builder()
                .id(1L)
                .odontologo(Odontologo.builder()
                        .id(1L)
                        .numeroDeMatricula("12345")
                        .nombre("Nicolas")
                        .apellido("Sanchez")
                        .build())
                .paciente(Paciente.builder().id(1L)
                        .dni(12345)
                        .nombre("Nicolas")
                        .apellido("Altez")
                        .fechaIngreso(LocalDate.now())
                        .domicilio(Domicilio.builder()
                                .id(1L)
                                .calle("Calle Falsa")
                                .numero(1234)
                                .localidad("Springfield")
                                .provincia("Springfield")
                                .build()).build())
                .fechaYHora(LocalDateTime.now())
                .build();

        turnoEntradaDTO = TurnoEntradaDTO.builder()
                .odontologoId(1L)
                .pacienteId(1L)
                .fechaYHora(LocalDateTime.now())
                .build();
    }

    @Test
    void deberiaLanzarUnaExcepcionCuandoElOdontologoYElIdNoExisten() {
        when(odontologoService.buscarOdontologoPorId(1L)).thenReturn(null);
        when(pacienteService.buscarPacientePorId(1L)).thenReturn(null);

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> {
            turnoService.registrarTurno(turnoEntradaDTO);
        });

        verify(odontologoService, times(1)).buscarOdontologoPorId(1L);
        verify(pacienteService, times(1)).buscarPacientePorId(1L);

        assertEquals("El odontologo y el paciente no existen", badRequestException.getMessage());
    }

    @Test
    void deberiaLanzarUnaExcepcionCuandoElOdontologoNoExiste() {
        when(odontologoService.buscarOdontologoPorId(1L)).thenReturn(null);
        when(pacienteService.buscarPacientePorId(1L)).thenReturn(paciente);

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> {
            turnoService.registrarTurno(turnoEntradaDTO);
        });

        verify(odontologoService, times(1)).buscarOdontologoPorId(anyLong());
        verify(pacienteService, times(1)).buscarPacientePorId(1L);

        assertEquals("El odontologo no existe", badRequestException.getMessage());
    }

    @Test
    void deberiaLanzarUnaExcepcionCuandoElPacienteNoExiste() {
        when(odontologoService.buscarOdontologoPorId(1L)).thenReturn(odontologo);
        when(pacienteService.buscarPacientePorId(1L)).thenReturn(null);

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> {
            turnoService.registrarTurno(turnoEntradaDTO);
        });

        verify(odontologoService, times(1)).buscarOdontologoPorId(1L);
        verify(pacienteService, times(1)).buscarPacientePorId(1L);

        assertEquals("El paciente no existe", badRequestException.getMessage());
    }

    @Test
    void deberiaRegistrarUnTurno_YRetornarUnTurnoConId() throws BadRequestException {
        when(odontologoService.buscarOdontologoPorId(1L)).thenReturn(odontologo);
        when(pacienteService.buscarPacientePorId(1L)).thenReturn(paciente);
        when(turnoRepository.save(any())).thenReturn(turno);

        TurnoSalidaDTO turnoSalidaDTO = turnoService.registrarTurno(turnoEntradaDTO);

        verify(odontologoService, times(1)).buscarOdontologoPorId(1L);
        verify(pacienteService, times(1)).buscarPacientePorId(1L);
        verify(turnoRepository, times(1)).save(any(Turno.class));

        assertNotNull(turnoSalidaDTO.getId());
        assertEquals(1L, turnoSalidaDTO.getId());

    }

    @Test
    void deberiaBuscarUnaListaDeTurnos_YRetornarUnaListaConElementos(){
        when(turnoRepository.findAll()).thenReturn(List.of(turno));
        List<TurnoSalidaDTO> turnos = turnoService.listarTurnos();

        verify(turnoRepository, times(1)).findAll();

        assertFalse(turnos.isEmpty());
        assertEquals(1, turnos.size());
        assertEquals(turno.getId(), turnos.get(0).getId());
    }

    @Test
    void deberiaBuscarUnaListaDeTurnos_YRetornarUnaListaVacia(){
        when(turnoRepository.findAll()).thenReturn(List.of());
        List<TurnoSalidaDTO> turnos = turnoService.listarTurnos();

        verify(turnoRepository, times(1)).findAll();

        assertTrue(turnos.isEmpty());
    }

    @Test
    void deberiaBuscarUnTurnoConId1_YRetornarElTurno(){
        when(turnoRepository.findById(1L)).thenReturn(java.util.Optional.of(turno));
        TurnoSalidaDTO turnoBuscado = turnoService.buscarTurnoPorId(1L);

        verify(turnoRepository, times(1)).findById(1L);

        assertNotNull(turnoBuscado);
        assertEquals(turno.getId(), turnoBuscado.getId());
    }

    @Test
    void deberiaBuscarUnTurnoConId1_YRetornarNull(){
        when(turnoRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());
        TurnoSalidaDTO turnoBuscado = turnoService.buscarTurnoPorId(1L);

        verify(turnoRepository, times(1)).findById(1L);

        assertNull(turnoBuscado);
    }

    @Test
    void deberiaEliminarUnTurnoConId1_YNoRetornarNada() throws ResourceNotFoundException {
        when(turnoRepository.existsById(1L)).thenReturn(true);
        turnoService.eliminarTurno(1L);

        verify(turnoRepository, times(1)).existsById(1L);
        verify(turnoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deberiaIntentarEliminarUnTurnoInexistente_YLanzarExcepcionConMensaje(){
        when(turnoRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> {
            turnoService.eliminarTurno(1L);
        });

        verify(turnoRepository, times(1)).existsById(1L);

        assertEquals("No se encontró el turno para eliminar con el id: 1", resourceNotFoundException.getMessage());
    }

    @Test
    void deberiaActualizarUnTurnoExistente() throws ResourceNotFoundException {
        LocalDateTime nuevaFechaYHora = LocalDateTime.of(2029, 10, 10, 10, 10);

        TurnoEntradaDTO turnoEntradaDTO = TurnoEntradaDTO.builder()
                .odontologoId(2L)
                .pacienteId(2L)
                .fechaYHora(nuevaFechaYHora)
                .build();

        Turno crearTurnoActualizado = Turno.builder().id(1L).odontologo(Odontologo.builder().id(2L).build()).paciente(Paciente.builder().id(2L).build()).fechaYHora(nuevaFechaYHora).build();

        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));
        when(odontologoService.buscarOdontologoPorId(2L)).thenReturn(odontologo);
        when(pacienteService.buscarPacientePorId(2L)).thenReturn(paciente);
        when(turnoRepository.save(any())).thenReturn(crearTurnoActualizado);

        TurnoSalidaDTO turnoActualizado = turnoService.actualizarTurno(turnoEntradaDTO, 1L);

        verify(turnoRepository, times(1)).findById(1L);
        verify(odontologoService, times(1)).buscarOdontologoPorId(2L);
        verify(pacienteService, times(1)).buscarPacientePorId(2L);
        verify(turnoRepository, times(1)).save(any(Turno.class));

        assertNotNull(turnoActualizado);
        assertEquals(turno.getId(), turnoActualizado.getId());
        assertNotEquals(turno.getFechaYHora(), turnoActualizado.getFechaYHora());
        assertNotEquals(turno.getOdontologo().getId(), turnoActualizado.getOdontologo().getId());
        assertNotEquals(turno.getPaciente().getId(), turnoActualizado.getPaciente().getId());
    }

    @Test
    void deberiaIntentarActualizarUnTurnoInexistente_YLanzarExcepcionConMensaje() {
        when(turnoRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> {
            turnoService.actualizarTurno(turnoEntradaDTO, 1L);
        });

        verify(turnoRepository, times(1)).findById(1L);
        verify(odontologoService, never()).buscarOdontologoPorId(anyLong());
        verify(pacienteService, never()).buscarPacientePorId(anyLong());
        verify(turnoRepository, never()).save(any(Turno.class));

        assertEquals("No se encontró el turno a actualizar con id: 1", resourceNotFoundException.getMessage());
    }


}