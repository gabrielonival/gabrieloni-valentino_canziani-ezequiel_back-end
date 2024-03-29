package com.backend.integrador.controller;

import com.backend.integrador.dto.odontologo.OdontologoEntradaDTO;
import com.backend.integrador.dto.odontologo.OdontologoSalidaDTO;
import com.backend.integrador.dto.paciente.PacienteEntradaDTO;
import com.backend.integrador.exception.ResourceNotFoundException;
import com.backend.integrador.repository.OdontologoRepository;
import com.backend.integrador.service.IOdontologoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OdontologoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IOdontologoService odontologoService;

    private OdontologoSalidaDTO odontologoSalidaDTO;

    @BeforeEach
    void setUp() {
        odontologoSalidaDTO = OdontologoSalidaDTO.builder()
                .id(1L)
                .nombre("nicolas")
                .apellido("altez")
                .numeroDeMatricula("matricula")
                .build();
    }

    @Nested
    class buscarOdontologoPorIdTests {

        @Test
        void dadoIdOdontologoValido_cuandoBuscarOdontologoPorId_entoncesRetornaOdontologoYOk() throws Exception {
            when(odontologoService.buscarOdontologoPorId(1L)).thenReturn(odontologoSalidaDTO);

            ResultActions respuesta = mockMvc.perform(get("/odontologos/1"));

            respuesta.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(odontologoSalidaDTO.getId()))
                    .andExpect(jsonPath("$.nombre").value(odontologoSalidaDTO.getNombre()))
                    .andExpect(jsonPath("$.apellido").value(odontologoSalidaDTO.getApellido()))
                    .andExpect(jsonPath("$.numeroDeMatricula").value(odontologoSalidaDTO.getNumeroDeMatricula()));

            verify(odontologoService, times(1)).buscarOdontologoPorId(1L);
        }

        @Test
        void dadoIdOdontologoInvalido_cuandoBuscarOdontologoPorId_entoncesRetornarOkYNull() throws Exception {
            when(odontologoService.buscarOdontologoPorId(1L)).thenReturn(null);

            ResultActions respuesta = mockMvc.perform(get("/odontologos/1"));

            respuesta.andExpect(status().isOk()).andExpect(content().string(""));

            verify(odontologoService, times(1)).buscarOdontologoPorId(1L);
        }
    }

    @Nested
    class BuscarTodosLosOdontologosTests {

        @Test
        void dadoListaOdontologosVacia_cuandoBuscarTodosLosOdontologos_EntoncesRetornaOkYListaVacia() throws Exception {
            when(odontologoService.buscarTodosLosOdontologos()).thenReturn(List.of());

            ResultActions respuesta = mockMvc.perform(get("/odontologos"));

            respuesta.andExpect(status().isOk()).andExpect(content().string("[]")).andExpect(jsonPath("$",hasSize(0)));

            verify(odontologoService, times(1)).buscarTodosLosOdontologos();
        }

        @Test
        void dadoListaOdontologosConElementos_cuandoListarOdontologos_EntonceesRetornaOkYListaDeOdontologos() throws Exception {
            when(odontologoService.buscarTodosLosOdontologos()).thenReturn(List.of(odontologoSalidaDTO));

            ResultActions respuesta = mockMvc.perform(get("/odontologos"));

            respuesta.andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").value(odontologoSalidaDTO.getId()))
                    .andExpect(jsonPath("$[0].nombre").value(odontologoSalidaDTO.getNombre()))
                    .andExpect(jsonPath("$[0].apellido").value(odontologoSalidaDTO.getApellido()))
                    .andExpect(jsonPath("$[0].numeroDeMatricula").value(odontologoSalidaDTO.getNumeroDeMatricula()));

            verify(odontologoService, times(1)).buscarTodosLosOdontologos();
        }
    }

    @Nested
    class guardarOdontologoTests {

        @Test
        void dadoOdontologoValido_CuandoGuardarOdontologo_EntoncesRetornaOdontologoYCreated() throws Exception {
            when(odontologoService.guardarOdontologo(any(OdontologoEntradaDTO.class))).thenReturn(odontologoSalidaDTO);

            ResultActions respuesta = mockMvc.perform(post("/odontologos")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(OdontologoEntradaDTO.builder()
                            .nombre("nicolas")
                            .apellido("altez")
                            .numeroDeMatricula("matricula")
                            .build())));

            respuesta.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(odontologoSalidaDTO.getId()))
                    .andExpect(jsonPath("$.nombre").value(odontologoSalidaDTO.getNombre()))
                    .andExpect(jsonPath("$.apellido").value(odontologoSalidaDTO.getApellido()))
                    .andExpect(jsonPath("$.numeroDeMatricula").value(odontologoSalidaDTO.getNumeroDeMatricula()));

            verify(odontologoService, times(1)).guardarOdontologo(any(OdontologoEntradaDTO.class));
        }

        @Test
        void dadoOdontologoDatosNullOVacios_CuandoGuardarOdontologo_EntoncesRetornaBadRequest() throws Exception {
            ResultActions respuesta = mockMvc.perform(post("/odontologos")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(OdontologoEntradaDTO.builder().build())));

            respuesta.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message").value("Validación fallida"))
                    .andExpect(jsonPath("$.errores[*]").isArray())
                    .andExpect(jsonPath("$.errores", hasItems(
                            "El numero de matricula del odontologo no puede ser nulo",
                            "El numero de matricula del odontologo no puede estar vacío",
                            "El nombre del odontologo no puede ser nulo",
                            "El nombre del odontologo no puede estar vacío",
                            "El apellido del paciente no puede ser nulo",
                            "El apellido del paciente no puede estar vacío"
                    )));
        }

        @Test
        void dadoOdontologoDatosConCaracteresMenorAlRequerido_EntoncesRetornarBadRequest() throws Exception {
            ResultActions respuesta = mockMvc.perform(post("/odontologos")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(OdontologoEntradaDTO.builder()
                            .nombre("a")
                            .apellido("a")
                            .numeroDeMatricula("a")
                            .build())));

            respuesta.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message").value("Validación fallida"))
                    .andExpect(jsonPath("$.errores[*]").isArray())
                    .andExpect(jsonPath("$.errores", hasItems(
                            "El numero de matricula del odontologo debe tener entre 3 y 50 caracteres",
                            "El nombre del odontologo debe tener entre 3 y 50 caracteres",
                            "El apellido del paciente debe tener entre 3 y 50 caracteres"
                    )));
        }

        @Test
        void dadoOdontologoConCaracteresMayorAlRequerido_EntoncesRetornarBadRequest() throws Exception {
            ResultActions respuesta = mockMvc.perform(post("/odontologos")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(OdontologoEntradaDTO.builder()
                            .nombre("a".repeat(51))
                            .apellido("a".repeat(51))
                            .numeroDeMatricula("a".repeat(51))
                            .build())));

            respuesta.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message").value("Validación fallida"))
                    .andExpect(jsonPath("$.errores[*]").isArray())
                    .andExpect(jsonPath("$.errores", hasItems(
                            "El numero de matricula del odontologo debe tener entre 3 y 50 caracteres",
                            "El nombre del odontologo debe tener entre 3 y 50 caracteres",
                            "El apellido del paciente debe tener entre 3 y 50 caracteres"
                    )));
        }
    }

    @Nested
    class eliminarOdontologoTests {
        @Test
        void dadoIdOdontologoValido_cuandoEliminarOdontologo_EntoncesRetornaNoContent() throws Exception {

            doNothing().when(odontologoService).eliminarOdontologo(1L);

            ResultActions respuesta = mockMvc.perform(delete("/odontologos/1"));

            respuesta.andExpect(status().isNoContent()).andExpect(content().string("Se borro correctamente el odontologo"));

            verify(odontologoService, times(1)).eliminarOdontologo(1L);
        }

        @Test
        void dadoIdOdontologoInvaliod_cuandoEliminarOdontologo_EntoncesRetornarNotFound() throws Exception {

            doAnswer(invocation -> {
                throw new ResourceNotFoundException("No se encontró el odontologo a eliminar con id: " + invocation.getArgument(0));
            }).when(odontologoService).eliminarOdontologo(1L);

            ResultActions respuesta = mockMvc.perform(delete("/odontologos/{id}", 1L));

            respuesta.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.mensaje").value("No se encontró el odontologo a eliminar con id: 1"))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"));

            verify(odontologoService, times(1)).eliminarOdontologo(1L);

        }

    }

    @Nested
    class actualizarOdontologoTests {

        @Test
        void dadoOdontologoValido_cuandoActualizarOdontologo_EntoncesRetornaOdontologoYOk() throws Exception {
            OdontologoEntradaDTO odontologoAActualizar = OdontologoEntradaDTO.builder()
                    .nombre("nuevoNombre")
                    .apellido("nuevoApellido")
                    .numeroDeMatricula("nuevoNumeroDeMatricula")
                    .build();

            OdontologoSalidaDTO odontologoActualizado = OdontologoSalidaDTO.builder()
                    .id(1L)
                    .nombre("nuevoNombre")
                    .apellido("nuevoApellido")
                    .numeroDeMatricula("nuevoNumeroDeMatricula")
                    .build();

            when(odontologoService.actualizarOdontologo(any(OdontologoEntradaDTO.class), eq(1L))).thenReturn(odontologoActualizado);

            ResultActions respuesta = mockMvc.perform(put("/odontologos/1")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(odontologoAActualizar)));

            respuesta.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(odontologoActualizado.getId()))
                    .andExpect(jsonPath("$.nombre").value(odontologoActualizado.getNombre()))
                    .andExpect(jsonPath("$.apellido").value(odontologoActualizado.getApellido()))
                    .andExpect(jsonPath("$.numeroDeMatricula").value(odontologoActualizado.getNumeroDeMatricula()));

            verify(odontologoService, times(1)).actualizarOdontologo(any(OdontologoEntradaDTO.class), eq(1L));
        }

        @Test
        void dadoOdontologoIdInvalido_CuandoActualizarOdontologo_entoncesRetornarNotFound() throws Exception {

            doAnswer(invocation -> {
                throw new ResourceNotFoundException("No se encontró el odontologo a actualizar con id: " + invocation.getArgument(1));
            }).when(odontologoService).actualizarOdontologo(any(OdontologoEntradaDTO.class), eq(1L));

            ResultActions respuesta = mockMvc.perform(put("/odontologos/1")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(OdontologoEntradaDTO.builder()
                            .nombre("nicolas")
                            .apellido("altez")
                            .numeroDeMatricula("matricula")
                            .build())));
            respuesta.andDo(print());
            respuesta.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.mensaje").value("No se encontró el odontologo a actualizar con id: 1"))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"));

            verify(odontologoService, times(1)).actualizarOdontologo(any(OdontologoEntradaDTO.class), eq(1L));

        }
    }

}