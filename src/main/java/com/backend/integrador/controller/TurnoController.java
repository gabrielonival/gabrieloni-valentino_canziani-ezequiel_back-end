package com.backend.integrador.controller;

import com.backend.integrador.dto.error.ErrorRespuestaDTO;
import com.backend.integrador.dto.odontologo.OdontologoSalidaDTO;
import com.backend.integrador.dto.paciente.PacienteSalidaDTO;
import com.backend.integrador.dto.turno.TurnoEntradaDTO;
import com.backend.integrador.dto.turno.TurnoSalidaDTO;
import com.backend.integrador.entity.Turno;
import com.backend.integrador.exception.BadRequestException;
import com.backend.integrador.exception.ResourceNotFoundException;
import com.backend.integrador.service.ITurnoService;
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
@RequestMapping("/turnos")
public class TurnoController {

    private ITurnoService turnoService;

    public TurnoController(ITurnoService turnoService) {
        this.turnoService = turnoService;
    }


    @Operation(summary = "Guardar un turno", description = "Guarda un turno en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Turno guardado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TurnoSalidaDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Error en los datos ingresados ",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRespuestaDTO.class)))
    })
    @PostMapping
    public ResponseEntity<TurnoSalidaDTO> registrarTurno(@RequestBody @Valid TurnoEntradaDTO turno) throws BadRequestException {
        return new ResponseEntity<>(turnoService.registrarTurno(turno), HttpStatus.CREATED);
    }
    @Operation(summary = "Búsqueda de todos los turnos", description = "Recupera todos los turnos de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turnos obtenidos correctamente",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TurnoSalidaDTO.class)))})
    })
    @GetMapping
    public ResponseEntity<List<TurnoSalidaDTO>> listarTurnos() {
        return new ResponseEntity<>(turnoService.listarTurnos(), HttpStatus.OK);
    }


    @Operation(summary = "Eliminar un turno", description = "Elimina un turno de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Turno eliminado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),

            @ApiResponse(responseCode = "404", description = "Turno a eliminar no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRespuestaDTO.class))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTurno(@PathVariable Long id) throws ResourceNotFoundException {
        turnoService.eliminarTurno(id);
        return new ResponseEntity<>("Se borro correctamente el turno", HttpStatus.NO_CONTENT);
    }
    @Operation(summary = "Actualizar un turno", description = "Actualiza un turno en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno actualizado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TurnoSalidaDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Error en los datos ingresados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRespuestaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Turno a actualizar no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRespuestaDTO.class)))})
    @PutMapping("/{id}")
    public ResponseEntity<TurnoSalidaDTO> actualizarTurno(@RequestBody @Valid TurnoEntradaDTO turnoEntradaDTO, @PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(turnoService.actualizarTurno(turnoEntradaDTO,id), HttpStatus.OK);
    }

    @Operation(summary = "Búsqueda de un turno por Id", description = "Recupera un turno por su identificador único.")
    @ApiResponse(responseCode = "200", description = "Turno obtenido correctamente",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TurnoSalidaDTO.class))})
    @GetMapping("/{id}")
    public ResponseEntity<TurnoSalidaDTO> buscarTurno(@PathVariable Long id) {
        return new ResponseEntity<>(turnoService.buscarTurnoPorId(id), HttpStatus.OK);
    }

}
