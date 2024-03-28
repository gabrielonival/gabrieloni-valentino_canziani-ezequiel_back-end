package com.backend.integrador.controller;

import com.backend.integrador.dto.error.ErrorRespuestaDTO;
import com.backend.integrador.dto.odontologo.OdontologoEntradaDTO;
import com.backend.integrador.dto.odontologo.OdontologoSalidaDTO;
import com.backend.integrador.exception.ResourceNotFoundException;
import com.backend.integrador.service.IOdontologoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/odontologos")
public class OdontologoController {

    private IOdontologoService odontologoService;

    public OdontologoController(IOdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }

    @Operation(summary = "Búsqueda de un odontólogo por Id", description = "Recupera un odontólogo por su identificador único.")
    @ApiResponse(responseCode = "200", description = "Odontólogo obtenido correctamente",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = OdontologoSalidaDTO.class))})
    @GetMapping("/{id}")
    public ResponseEntity<OdontologoSalidaDTO> buscarOdontologoPorId(@PathVariable Long id) {
        return new ResponseEntity<>(odontologoService.buscarOdontologoPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Búsqueda de todos los odontólogos", description = "Recupera todos los odontólogos de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Odontólogos obtenidos correctamente",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OdontologoSalidaDTO.class)))})
    })
    @GetMapping
    public ResponseEntity<List<OdontologoSalidaDTO>> buscarTodosLosOdontologos() {
        return new ResponseEntity<>(odontologoService.buscarTodosLosOdontologos(), HttpStatus.OK);
    }


    @Operation(summary = "Guardar un odontólogo", description = "Guarda un odontólogo en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Odontólogo guardado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OdontologoSalidaDTO.class))}),

            @ApiResponse(responseCode = "400", description = "Error en los datos ingresados ",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRespuestaDTO.class)))
    })
    @PostMapping
    public ResponseEntity<OdontologoSalidaDTO> guardarOdontologo(@RequestBody @Valid OdontologoEntradaDTO odontologo) {
        return new ResponseEntity<>(odontologoService.guardarOdontologo(odontologo), HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar un odontólogo", description = "Elimina un odontólogo de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Odontólogo eliminado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),

            @ApiResponse(responseCode = "404", description = "Odontólogo a eliminar no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRespuestaDTO.class))),
    })
    @DeleteMapping("{id}")
    public ResponseEntity<String> eliminarOdontologo(@PathVariable Long id) throws ResourceNotFoundException {
        odontologoService.eliminarOdontologo(id);
        return new ResponseEntity<>("Se borro correctamente el odontologo", HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Actualizar un odontólogo", description = "Actualiza un odontólogo en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Odontólogo actualizado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OdontologoSalidaDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Odontólogo a actualizar no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRespuestaDTO.class)))})
    @PutMapping("{id}")
    public ResponseEntity<OdontologoSalidaDTO> actualizarOdontologo(@RequestBody @Valid OdontologoEntradaDTO odontologo, @PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(odontologoService.actualizarOdontologo(odontologo, id), HttpStatus.OK);
    }

}
