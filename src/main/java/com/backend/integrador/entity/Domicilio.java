package com.backend.integrador.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "DOMICILIOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Domicilio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String calle;
    @Column(nullable = false)
    private int numero;
    @Column(nullable = false)
    private String localidad;
    @Column(nullable = false)
    private String provincia;
}
