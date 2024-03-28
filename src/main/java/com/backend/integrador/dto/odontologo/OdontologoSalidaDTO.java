package com.backend.integrador.dto.odontologo;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OdontologoSalidaDTO {

    private Long id;
    private String numeroDeMatricula;
    private String nombre;
    private String apellido;


}
