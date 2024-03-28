package com.backend.integrador.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ODONTOLOGOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Odontologo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NUMERO_DE_MATRICULA", nullable = false, length = 50, unique = true)
    private String numeroDeMatricula;
    @Column(nullable = false, length = 50)
    private String nombre;
    @Column(nullable = false, length = 50)
    private String apellido;

}
