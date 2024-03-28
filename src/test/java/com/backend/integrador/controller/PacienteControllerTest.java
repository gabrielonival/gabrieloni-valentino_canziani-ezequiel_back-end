package com.backend.integrador.controller;

import com.backend.integrador.dto.domicilio.DomicilioEntradaDTO;
import com.backend.integrador.dto.domicilio.DomicilioSalidaDTO;
import com.backend.integrador.dto.odontologo.OdontologoEntradaDTO;
import com.backend.integrador.dto.paciente.PacienteEntradaDTO;
import com.backend.integrador.dto.paciente.PacienteSalidaDTO;
import com.backend.integrador.entity.Domicilio;
import com.backend.integrador.entity.Paciente;
import com.backend.integrador.exception.ResourceNotFoundException;
import com.backend.integrador.repository.PacienteRepository;
import com.backend.integrador.service.IPacienteService;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IPacienteService pacienteService;

    private PacienteEntradaDTO pacienteEntradaDTO;
    private PacienteSalidaDTO pacienteSalidaDTO;

    @BeforeEach
    void setUp() {

        pacienteEntradaDTO = PacienteEntradaDTO.builder()
                .nombre("nicolas")
                .apellido("altez")
                .dni(12345)
                .fechaIngreso(LocalDate.of(2027, 10, 10))
                .domicilioEntradaDTO(DomicilioEntradaDTO.builder()
                        .calle("Avenida Libertador")
                        .numero(123)
                        .localidad("Uruguay")
                        .provincia("Canelones")
                        .build())
                .build();

        pacienteSalidaDTO = PacienteSalidaDTO.builder()
                .id(1L)
                .nombre("nicolas")
                .apellido("altez")
                .dni(12345)
                .fechaIngreso(LocalDate.of(2027, 10, 10))
                .domicilioSalidaDTO(DomicilioSalidaDTO.builder()
                        .calle("Avenida Libertador")
                        .numero(123)
                        .localidad("Uruguay")
                        .provincia("Canelones")
                        .build())
                .build();
    }

    @Nested
    class buscarPacientePorIdTest {

        @Test
        void dadoIdPacienteValido_cuandoBuscarPacientePorId_entoncesRetornaPacienteYOk() throws Exception {

            when(pacienteService.buscarPacientePorId(1L)).thenReturn(pacienteSalidaDTO);

            ResultActions respuesta = mockMvc.perform(get("/pacientes/{id}", 1));

            respuesta.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(pacienteSalidaDTO.getId()))
                    .andExpect(jsonPath("$.nombre").value(pacienteSalidaDTO.getNombre()))
                    .andExpect(jsonPath("$.apellido").value(pacienteSalidaDTO.getApellido()))
                    .andExpect(jsonPath("$.dni").value(pacienteSalidaDTO.getDni()))
                    .andExpect(jsonPath("$.domicilioSalidaDTO.calle").value(pacienteSalidaDTO.getDomicilioSalidaDTO().getCalle()))
                    .andExpect(jsonPath("$.domicilioSalidaDTO.numero").value(pacienteSalidaDTO.getDomicilioSalidaDTO().getNumero()))
                    .andExpect(jsonPath("$.domicilioSalidaDTO.localidad").value(pacienteSalidaDTO.getDomicilioSalidaDTO().getLocalidad()))
                    .andExpect(jsonPath("$.domicilioSalidaDTO.provincia").value(pacienteSalidaDTO.getDomicilioSalidaDTO().getProvincia()));

            verify(pacienteService, times(1)).buscarPacientePorId(1L);
        }


        @Test
        void dadoIdPacienteInvalido_cuandoBuscarPacientePorId_entoncesRetornaOkYNull() throws Exception {
            when(pacienteService.buscarPacientePorId(anyLong())).thenReturn(null);

            ResultActions respuesta = mockMvc.perform(get("/pacientes/{id}", 1L));

            respuesta.andExpect(status().isOk()).andExpect(content().string(""));

            verify(pacienteService, times(1)).buscarPacientePorId(1L);
        }
    }

    @Nested
    class guardarPacienteTests {

        @Test
        void dadoEmpleadoValido_cuandoGuardarPaciente_EntoncesRetornaIsCreatedYPaciente() throws Exception {
            when(pacienteService.guardarPaciente(any(PacienteEntradaDTO.class))).thenReturn(pacienteSalidaDTO);

            ResultActions respuesta = mockMvc.perform(post("/pacientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pacienteEntradaDTO)));

            respuesta.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.nombre").value(pacienteEntradaDTO.getNombre()))
                    .andExpect(jsonPath("$.apellido").value(pacienteEntradaDTO.getApellido()))
                    .andExpect(jsonPath("$.dni").value(pacienteEntradaDTO.getDni()))
                    .andExpect(jsonPath("$.domicilioSalidaDTO.calle").value(pacienteEntradaDTO.getDomicilioEntradaDTO().getCalle()))
                    .andExpect(jsonPath("$.domicilioSalidaDTO.numero").value(pacienteEntradaDTO.getDomicilioEntradaDTO().getNumero()))
                    .andExpect(jsonPath("$.domicilioSalidaDTO.localidad").value(pacienteEntradaDTO.getDomicilioEntradaDTO().getLocalidad()))
                    .andExpect(jsonPath("$.domicilioSalidaDTO.provincia").value(pacienteEntradaDTO.getDomicilioEntradaDTO().getProvincia()));

            verify(pacienteService, times(1)).guardarPaciente(any(PacienteEntradaDTO.class));
        }

        @Test
        void dadoPacienteDatosNullOVacios_cuandoGuardarPaciente_EntoncesRetornaBadRequest() throws Exception {

            ResultActions respuesta = mockMvc.perform(post("/pacientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(PacienteEntradaDTO.builder().domicilioEntradaDTO(DomicilioEntradaDTO.builder().build()).build())));


            respuesta.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message").value("Validación fallida"))
                    .andExpect(jsonPath("$.errores[*]").isArray())
                    .andExpect(jsonPath("$.errores", hasItems(
                            "El nombre del paciente no puede ser nulo",
                            "El dni del paciente no puede ser nulo ni negativo",
                            "El campo localidad no puede estar en blanco",
                            "La fecha de ingreso no puede ser nula",
                            "El campo calle no puede ser nulo",
                            "El apellido del paciente no puede ser nulo",
                            "El nombre del paciente no puede estar vacío",
                            "El campo localidad no puede ser nulo",
                            "El campo provincia no puede estar en blanco",
                            "El campo provincia no puede ser nulo",
                            "El numero no puede ser nulo o menor a cero",
                            "El campo calle no puede estar en blanco",
                            "El apellido del paciente no puede estar vacío"
                    )))
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"));

            verify(pacienteService, never()).guardarPaciente(any(PacienteEntradaDTO.class));

        }

        @Test
        void dadoPacienteDatosConCaracteresMenorAlRequerido_EntoncesRetornarBadRequest() throws Exception {
            PacienteEntradaDTO paciente = PacienteEntradaDTO.builder()
                    .nombre("a")
                    .apellido("b")
                    .dni(12334)
                    .fechaIngreso(LocalDate.of(2022, 10, 12))
                    .domicilioEntradaDTO(DomicilioEntradaDTO.builder()
                            .calle("c")
                            .numero(1234)
                            .localidad("d")
                            .provincia("e")
                            .build())
                    .build();

            ResultActions respuesta = mockMvc.perform(post("/pacientes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(paciente)));

            respuesta.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message").value("Validación fallida"))
                    .andExpect(jsonPath("$.errores[*]").isArray())
                    .andExpect(jsonPath("$.errores", hasItems(
                            "El nombre del paciente debe tener entre 3 y 50 caracteres",
                            "El apellido del paciente debe tener entre 3 y 50 caracteres",
                            "La calle debe tener entre 3 y 255 caracteres",
                            "La localidad debe tener entre 2 y 255 caracteres",
                            "La provincia debe tener entre 2 y 255 caracteres"
                    )))
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"));

            verify(pacienteService, never()).guardarPaciente(any(PacienteEntradaDTO.class));
        }

        @Test
        void dadoPacienteDatosConCaracteresMayorAlRequerido_EntoncesRetornarBadRequest() throws Exception {
            String mayorA50 = "e".repeat(56);
            String mayorA255 = "e".repeat(257);
            int mayorA8Digitos = 123456789;
            PacienteEntradaDTO paciente = PacienteEntradaDTO.builder()
                    .nombre(mayorA50)
                    .apellido(mayorA50)
                    .dni(mayorA8Digitos)
                    .fechaIngreso(LocalDate.of(2022, 10, 12))
                    .domicilioEntradaDTO(DomicilioEntradaDTO.builder()
                            .calle(mayorA255)
                            .numero(mayorA8Digitos)
                            .localidad(mayorA255)
                            .provincia(mayorA255)
                            .build())
                    .build();

            ResultActions respuesta = mockMvc.perform(post("/pacientes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(paciente)));

            respuesta.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message").value("Validación fallida"))
                    .andExpect(jsonPath("$.errores[*]").isArray())
                    .andExpect(jsonPath("$.errores", hasItems(
                            "El nombre del paciente debe tener entre 3 y 50 caracteres",
                            "El apellido del paciente debe tener entre 3 y 50 caracteres",
                            "La calle debe tener entre 3 y 255 caracteres",
                            "La localidad debe tener entre 2 y 255 caracteres",
                            "La provincia debe tener entre 2 y 255 caracteres",
                            "El número debe tener como máximo 8 dígitos",
                            "El Dni debe tener como máximo 8 dígitos"
                    )))
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"));

            verify(pacienteService, never()).guardarPaciente(any(PacienteEntradaDTO.class));
        }

        @Test
        void dadoPacienteDatoFechaInvalida_EntoncesRetornarBadRequest() throws Exception {

            PacienteEntradaDTO paciente = PacienteEntradaDTO.builder()
                    .nombre("nicolas")
                    .apellido("altez")
                    .dni(12345)
                    .fechaIngreso(LocalDate.now().minusDays(1))
                    .domicilioEntradaDTO(DomicilioEntradaDTO.builder()
                            .calle("Avenida Libertador")
                            .numero(123)
                            .localidad("Uruguay")
                            .provincia("Canelones")
                            .build())
                    .build();

            ResultActions respuesta = mockMvc.perform(post("/pacientes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(paciente)));

            respuesta.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message").value("Validación fallida"))
                    .andExpect(jsonPath("$.errores[*]").isArray())
                    .andExpect(jsonPath("$.errores", hasItems(
                            "La fecha de ingreso debe ser posterior o igual a la fecha actual"
                    )))
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"));

            verify(pacienteService, never()).guardarPaciente(any(PacienteEntradaDTO.class));
        }
    }

    @Nested
    class listarPacientesTests {

        @Test
        void dadoListaPacientesVacia_cuandoListarPacientes_EntoncesRetornaOkYListaVacia() throws Exception {
            when(pacienteService.listarPacientes()).thenReturn(List.of());

            ResultActions respuesta = mockMvc.perform(get("/pacientes"));

            respuesta.andExpect(status().isOk())
                    .andExpect(content().string("[]"))
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(pacienteService, times(1)).listarPacientes();
        }

        @Test
        void dadoListaPacientesConElementos_cuandoListarPacientes_EntoncesRetornaOkYListaPacientes() throws Exception {
            when(pacienteService.listarPacientes()).thenReturn(List.of(pacienteSalidaDTO));

            ResultActions respuesta = mockMvc.perform(get("/pacientes"));

            respuesta.andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").value(pacienteSalidaDTO.getId()))
                    .andExpect(jsonPath("$[0].nombre").value(pacienteSalidaDTO.getNombre()))
                    .andExpect(jsonPath("$[0].apellido").value(pacienteSalidaDTO.getApellido()))
                    .andExpect(jsonPath("$[0].dni").value(pacienteSalidaDTO.getDni()))
                    .andExpect(jsonPath("$[0].domicilioSalidaDTO.calle").value(pacienteSalidaDTO.getDomicilioSalidaDTO().getCalle()))
                    .andExpect(jsonPath("$[0].domicilioSalidaDTO.numero").value(pacienteSalidaDTO.getDomicilioSalidaDTO().getNumero()))
                    .andExpect(jsonPath("$[0].domicilioSalidaDTO.localidad").value(pacienteSalidaDTO.getDomicilioSalidaDTO().getLocalidad()))
                    .andExpect(jsonPath("$[0].domicilioSalidaDTO.provincia").value(pacienteSalidaDTO.getDomicilioSalidaDTO().getProvincia()));

            verify(pacienteService, times(1)).listarPacientes();
        }
    }

    @Nested
    class eliminarPacientesTests {

        @Test
        void dadoIdPacienteValido_cuandoEliminarPaciente_EntoncesRetornaOk() throws Exception {

            doNothing().when(pacienteService).eliminarPaciente(1L);


            ResultActions respuesta = mockMvc.perform(delete("/pacientes/{id}", 1L));

            respuesta.andExpect(status().isNoContent())
                    .andExpect(content().string("Se borro correctamente el paciente"));

            verify(pacienteService, times(1)).eliminarPaciente(anyLong());
        }

        @Test
        void dadoIdPacienteInvaliod_cuandoEliminarPaciente_EntoncesRetornarNotFound() throws Exception {

            doAnswer(invocation -> {
                throw new ResourceNotFoundException("No se encontró el paciente a eliminar con id: " + invocation.getArgument(0));
            }).when(pacienteService).eliminarPaciente(1L);



            ResultActions respuesta = mockMvc.perform(delete("/pacientes/1"));

            respuesta.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.mensaje").value("No se encontró el paciente a eliminar con id: 1"))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"));

            verify(pacienteService, times(1)).eliminarPaciente(1L);

        }
    }

    @Nested
    class actualizarPacienteTests {

        @Test
        void dadoIdPacienteValido_cuandoActualizarPacienteEntoncesRetornarOk() throws Exception {


            PacienteEntradaDTO pacienteAActualizar = PacienteEntradaDTO.builder()
                    .nombre("nombreActualizado")
                    .apellido("apellidoActualizado")
                    .fechaIngreso(LocalDate.of(2035, 12, 12))
                    .domicilioEntradaDTO(DomicilioEntradaDTO.builder()
                            .numero(312)
                            .provincia("provinciaActualizada")
                            .calle("calleActualizada")
                            .localidad("localidadActualizada")
                            .build())
                    .dni(54321)
                    .build();

            PacienteSalidaDTO pacienteActualizado = PacienteSalidaDTO.builder()
                    .id(1L)
                    .nombre("nombreActualizado")
                    .apellido("apellidoActualizado")
                    .dni(54321)
                    .fechaIngreso(LocalDate.of(2035, 12, 12))
                    .domicilioSalidaDTO(DomicilioSalidaDTO.builder()
                            .id(1L)
                            .provincia("provinciaActualizada")
                            .calle("calleActualizada")
                            .numero(312)
                            .localidad("localidadActualizada")
                            .build())
                    .build();

            when(pacienteService.actualizarPaciente(any(PacienteEntradaDTO.class), eq(1L))).thenReturn(pacienteActualizado);

            ResultActions respuesta = mockMvc.perform(put("/pacientes/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(pacienteAActualizar)));


            respuesta.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(pacienteActualizado.getId()))
                    .andExpect(jsonPath("$.nombre").value(pacienteActualizado.getNombre()))
                    .andExpect(jsonPath("$.apellido").value(pacienteActualizado.getApellido()))
                    .andExpect(jsonPath("$.dni").value(pacienteActualizado.getDni()))
                    .andExpect(jsonPath("$.domicilioSalidaDTO.calle").value(pacienteActualizado.getDomicilioSalidaDTO().getCalle()))
                    .andExpect(jsonPath("$.domicilioSalidaDTO.numero").value(pacienteActualizado.getDomicilioSalidaDTO().getNumero()))
                    .andExpect(jsonPath("$.domicilioSalidaDTO.localidad").value(pacienteActualizado.getDomicilioSalidaDTO().getLocalidad()))
                    .andExpect(jsonPath("$.domicilioSalidaDTO.provincia").value(pacienteActualizado.getDomicilioSalidaDTO().getProvincia()));

            verify(pacienteService, times(1)).actualizarPaciente(any(PacienteEntradaDTO.class), eq(1L));

        }

        @Test
        void dadoIdPacienteInvalido_cuandoActualizarPacienteEntoncesRetornarNotFound() throws Exception {

            PacienteEntradaDTO pacienteAActualizar = PacienteEntradaDTO.builder()
                    .nombre("nombreActualizado")
                    .apellido("apellidoActualizado")
                    .fechaIngreso(LocalDate.of(2035, 12, 12))
                    .domicilioEntradaDTO(DomicilioEntradaDTO.builder()
                            .numero(312)
                            .provincia("provinciaActualizada")
                            .calle("calleActualizada")
                            .localidad("localidadActualizada")
                            .build())
                    .dni(54321)
                    .build();

            doAnswer(invocation -> {
                throw new ResourceNotFoundException("No se encontró el paciente a actualizar con id: " + invocation.getArgument(1));
            }).when(pacienteService).actualizarPaciente(any(PacienteEntradaDTO.class), eq(1L));



            ResultActions respuesta = mockMvc.perform(put("/pacientes/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(pacienteAActualizar)));

            respuesta.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.mensaje").value("No se encontró el paciente a actualizar con id: 1"))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"));

            verify(pacienteService, times(1)).actualizarPaciente(any(PacienteEntradaDTO.class), eq(1L));
        }

    }


}