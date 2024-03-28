package com.backend.integrador.dto.domicilio;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DomicilioSalidaDTO {
    private Long id;
    private String calle;
    private int numero;
    private String localidad;
    private String provincia;

}
