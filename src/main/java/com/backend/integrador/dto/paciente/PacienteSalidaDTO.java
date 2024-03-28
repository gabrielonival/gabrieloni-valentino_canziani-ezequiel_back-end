package com.backend.integrador.dto.paciente;

import com.backend.integrador.dto.domicilio.DomicilioSalidaDTO;
import lombok.*;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PacienteSalidaDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private int dni;
    private LocalDate fechaIngreso;
    private DomicilioSalidaDTO domicilioSalidaDTO;


}
