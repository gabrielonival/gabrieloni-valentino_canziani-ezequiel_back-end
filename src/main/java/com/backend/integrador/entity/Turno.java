package com.backend.integrador.entity;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "TURNOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ODONTOLOGO_ID", referencedColumnName = "ID")
    private Odontologo odontologo;

    @ManyToOne
    @JoinColumn(name = "PACIENTE_ID", referencedColumnName = "ID")
    private Paciente paciente;
    @Column(name = "FECHA_Y_HORA", nullable = false)
    private LocalDateTime fechaYHora;
}
