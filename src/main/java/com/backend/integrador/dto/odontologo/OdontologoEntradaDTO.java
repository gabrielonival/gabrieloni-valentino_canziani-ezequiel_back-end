package com.backend.integrador.dto.odontologo;



import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OdontologoEntradaDTO {

    @NotNull(message = "El numero de matricula del odontologo no puede ser nulo")
    @NotBlank(message = "El numero de matricula del odontologo no puede estar vacío")
    @Size(min = 2, max = 50, message = "El numero de matricula del odontologo debe tener entre 3 y 50 caracteres")
    private String numeroDeMatricula;

    @NotNull(message = "El nombre del odontologo no puede ser nulo")
    @NotBlank(message = "El nombre del odontologo no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre del odontologo debe tener entre 3 y 50 caracteres")
    private String nombre;


    @NotNull(message = "El apellido del paciente no puede ser nulo")
    @NotBlank(message = "El apellido del paciente no puede estar vacío")
    @Size(min = 2, max = 50, message = "El apellido del paciente debe tener entre 3 y 50 caracteres")
    private String apellido;

}
