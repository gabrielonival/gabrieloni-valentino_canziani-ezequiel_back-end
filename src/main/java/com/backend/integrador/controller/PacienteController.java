package com.backend.integrador.controller;

import com.backend.integrador.dto.error.ErrorRespuestaDTO;
import com.backend.integrador.dto.odontologo.OdontologoSalidaDTO;
import com.backend.integrador.dto.paciente.PacienteEntradaDTO;
import com.backend.integrador.dto.paciente.PacienteSalidaDTO;
import com.backend.integrador.exception.ResourceNotFoundException;
import com.backend.integrador.service.IPacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    private IPacienteService pacienteService;

    public PacienteController(IPacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @Operation(summary = "Búsqueda de un paciente por Id", description = "Recupera un paciente por su identificador único.")
    @ApiResponse(responseCode = "200", description = "Paciente obtenido correctamente",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = PacienteSalidaDTO.class))})
    @GetMapping("/{id}")
    public ResponseEntity<PacienteSalidaDTO> buscarPacientePorId(@PathVariable Long id) {
        return new ResponseEntity<>(pacienteService.buscarPacientePorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Búsqueda de todos los pacientes", description = "Recupera todos los pacientes de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacientes obtenidos correctamente",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PacienteSalidaDTO.class)))})
    })
    @GetMapping
    public ResponseEntity<List<PacienteSalidaDTO>> listarPacientes() {
        return new ResponseEntity<>(pacienteService.listarPacientes(), HttpStatus.OK);
    }

    @Operation(summary = "Guardar un paciente", description = "Guarda un paciente en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente guardado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacienteSalidaDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Error en los datos ingresados ",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRespuestaDTO.class)))
    })
    @PostMapping
    public ResponseEntity<PacienteSalidaDTO> guardarPaciente(@RequestBody @Valid PacienteEntradaDTO paciente) {
        return new ResponseEntity<>(pacienteService.guardarPaciente(paciente), HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar un paciente", description = "Elimina un paciente de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente eliminado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),

            @ApiResponse(responseCode = "404", description = "Paciente a eliminar no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRespuestaDTO.class))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPaciente(@PathVariable Long id) throws ResourceNotFoundException {
        pacienteService.eliminarPaciente(id);
        return new ResponseEntity<>("Se borro correctamente el paciente", HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "Actualizar un paciente", description = "Actualiza un paciente en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente actualizado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacienteSalidaDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Paciente a actualizar no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRespuestaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error en los datos ingresados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRespuestaDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<PacienteSalidaDTO> actualizarPaciente(@RequestBody @Valid PacienteEntradaDTO paciente, @PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(pacienteService.actualizarPaciente(paciente, id), HttpStatus.OK);
    }

}
