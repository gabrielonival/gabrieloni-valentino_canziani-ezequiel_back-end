package com.backend.integrador.service.impl;

import com.backend.integrador.dto.odontologo.OdontologoEntradaDTO;
import com.backend.integrador.dto.odontologo.OdontologoSalidaDTO;
import com.backend.integrador.entity.Odontologo;
import com.backend.integrador.exception.ResourceNotFoundException;
import com.backend.integrador.repository.OdontologoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class OdontologoServiceImplTest {

    @Mock
    private OdontologoRepository odontologoRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private OdontologoServiceImpl odontologoService;

    private OdontologoEntradaDTO odontologoEntradaDTO;

    private Odontologo odontologo;

    @BeforeEach
    void setUp() {
        odontologoEntradaDTO = OdontologoEntradaDTO.builder()
                .nombre("Nicolas")
                .apellido("Altez")
                .numeroDeMatricula("12345")
                .build();

        odontologo = Odontologo.builder()
                .id(1L)
                .nombre("Nicolas")
                .apellido("Altez")
                .numeroDeMatricula("12345")
                .build();
    }

    @Test
    void deberiaGuardarUnOdontologo_YRetornarUnOdontologoConId() {
        when(odontologoRepository.save(any(Odontologo.class))).thenReturn(odontologo);

        OdontologoSalidaDTO odontologoGuardado = odontologoService.guardarOdontologo(odontologoEntradaDTO);

        verify(odontologoRepository, times(1)).save(any(Odontologo.class));

        assertNotNull(odontologoGuardado.getId());
    }

    @Test
    void deberiaBuscarUnaListaDeOdontologos_YRetornarUnaListaConELementos() {
        when(odontologoRepository.findAll()).thenReturn(List.of(odontologo));

        List<OdontologoSalidaDTO> odontologos = odontologoService.buscarTodosLosOdontologos();

        verify(odontologoRepository, times(1)).findAll();

        assertFalse(odontologos.isEmpty());
        assertEquals(1, odontologos.size());
        assertEquals(odontologo.getId(), odontologos.get(0).getId());
    }

    @Test
    void deberiaBuscarUnaListaDeOdontologos_YRetornarUnaListaVacia() {
        when(odontologoRepository.findAll()).thenReturn(List.of());

        List<OdontologoSalidaDTO> odontologos = odontologoService.buscarTodosLosOdontologos();

        verify(odontologoRepository, times(1)).findAll();

        assertTrue(odontologos.isEmpty());
    }

    @Test
    void deberiaBuscarUnOdontologoPorId_YRetornarElOdontologoEncontrado() {
        when(odontologoRepository.findById(1L)).thenReturn(Optional.of(odontologo));

        OdontologoSalidaDTO odontologoBuscado = odontologoService.buscarOdontologoPorId(1L);

        verify(odontologoRepository, times(1)).findById(1L);

        assertNotNull(odontologoBuscado);
        assertEquals(odontologo.getId(), odontologoBuscado.getId());
    }

    @Test
    void deberiaBuscarUnOdontologoPorId_YRetornarNull() {
        when(odontologoRepository.findById(anyLong())).thenReturn(Optional.empty());

        OdontologoSalidaDTO odontologoBuscado = odontologoService.buscarOdontologoPorId(1L);

        verify(odontologoRepository, times(1)).findById(1L);

        assertNull(odontologoBuscado);
    }

    @Test
    void deberiaIntentarEliminarUnOdontologoInexistente_YLanzarExcepcionConMensaje() {
        when(odontologoRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> {
            odontologoService.eliminarOdontologo(1L);
        });

        verify(odontologoRepository, times(1)).existsById(1L);
        verify(odontologoRepository, never()).deleteById(1L);
        assertEquals("No se encontró el odontologo a eliminar con id: 1", resourceNotFoundException.getMessage());
    }

    @Test
    void deberiaEliminarUnOdontologoExistente_YNoRetornarNada() throws ResourceNotFoundException {
        when(odontologoRepository.existsById(1L)).thenReturn(true);
        odontologoService.eliminarOdontologo(1L);

        verify(odontologoRepository, times(1)).existsById(1L);
        verify(odontologoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deberiaActualizarUnOdontologoExistente_YRetornarElOdontologoActualizado() throws ResourceNotFoundException {

        OdontologoEntradaDTO odontologoAModificar = OdontologoEntradaDTO.builder()
                .nombre("Maximiliano")
                .apellido("Acosta")
                .numeroDeMatricula("123456")
                .build();

        Odontologo crearOdontologoActualizado = Odontologo.builder()
                .id(1L)
                .nombre("Maximiliano")
                .apellido("Acosta")
                .numeroDeMatricula("123456")
                .build();

        when(odontologoRepository.findById(1L)).thenReturn(Optional.of(odontologo));
        when(odontologoRepository.save(any(Odontologo.class))).thenReturn(crearOdontologoActualizado);


        OdontologoSalidaDTO odontologoActualizado = odontologoService.actualizarOdontologo(odontologoAModificar, 1L);

        verify(odontologoRepository, times(1)).findById(1L);
        verify(odontologoRepository, times(1)).save(any(Odontologo.class));

        assertNotNull(odontologoActualizado);
        assertEquals(odontologo.getId(), odontologoActualizado.getId());
        assertNotEquals(odontologo.getNombre(), odontologoActualizado.getNombre());
        assertNotEquals(odontologo.getNumeroDeMatricula(), odontologoActualizado.getNumeroDeMatricula());
        assertNotEquals(odontologo.getApellido(), odontologoActualizado.getApellido());
    }

    @Test
    void deberiaIntentarActualizarUnOdontologoInexistente_YLanzarExcepcionConMensaje() {
        OdontologoEntradaDTO odontologoAModificar = OdontologoEntradaDTO.builder()
                .nombre("Maximiliano")
                .apellido("Acosta")
                .numeroDeMatricula("123456")
                .build();

        when(odontologoRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> {
            odontologoService.actualizarOdontologo(odontologoAModificar, 1L);
        });

        verify(odontologoRepository, times(1)).findById(1L);
        verify(odontologoRepository, never()).save(any(Odontologo.class));
        assertEquals("No se encontró el odontologo a actualizar con id: 1", resourceNotFoundException.getMessage());
    }

}