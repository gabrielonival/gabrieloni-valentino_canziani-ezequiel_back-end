package com.backend.integrador.dto.paciente;

import com.backend.integrador.dto.domicilio.DomicilioEntradaDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PacienteEntradaDTO {
    @NotNull(message = "El nombre del paciente no puede ser nulo")
    @NotBlank(message = "El nombre del paciente no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre del paciente debe tener entre 3 y 50 caracteres")
    private String nombre;

    @NotNull(message = "El apellido del paciente no puede ser nulo")
    @NotBlank(message = "El apellido del paciente no puede estar vacío")
    @Size(min = 2, max = 50, message = "El apellido del paciente debe tener entre 3 y 50 caracteres")
    private String apellido;

    @Positive(message = "El dni del paciente no puede ser nulo ni negativo")
    @Digits(integer = 8, fraction = 0, message = "El Dni debe tener como máximo 8 dígitos")
    private int dni;

    @FutureOrPresent(message = "La fecha de ingreso debe ser posterior o igual a la fecha actual")
    @NotNull(message = "La fecha de ingreso no puede ser nula")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaIngreso;

    @NotNull(message = "El domicilio del paciente no puede ser nulo")
    @Valid
    private DomicilioEntradaDTO domicilioEntradaDTO;

}
