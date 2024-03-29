package com.backend.integrador.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
     void obtenerFechaHoraActualFormateada_DebeRetornarFechaHoraFormateadaCorrectamente() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        String formatoEsperado = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String formatoActual = DateUtils.obtenerFechaHoraActualFormateada();

        assertEquals(formatoEsperado, formatoActual);
    }

}