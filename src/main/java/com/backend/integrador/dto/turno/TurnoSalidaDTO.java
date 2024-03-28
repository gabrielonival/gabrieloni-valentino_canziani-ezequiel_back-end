package com.backend.integrador.dto.turno;

import com.backend.integrador.dto.odontologo.OdontologoSalidaDTO;
import com.backend.integrador.dto.paciente.PacienteSalidaDTO;
import lombok.*;


import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TurnoSalidaDTO {
    private Long id;
    private OdontologoSalidaDTO odontologo;
    private PacienteSalidaDTO paciente;
    private LocalDateTime fechaYHora;
}
