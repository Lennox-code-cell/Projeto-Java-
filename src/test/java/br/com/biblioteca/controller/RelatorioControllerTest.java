package br.com.biblioteca.controller;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RelatorioControllerTest {
    private final RelatorioController controller = new RelatorioController();

    @Test
    void rejeitaPeriodoIncompletoOuInvertidoAntesDeAcessarBanco() {
        assertThrows(IllegalArgumentException.class,
                () -> controller.gerarPorPeriodo(null, LocalDate.now(), "todos"));
        assertThrows(IllegalArgumentException.class,
                () -> controller.gerarPorPeriodo(
                        LocalDate.now(), LocalDate.now().minusDays(1), "todos"));
    }

    @Test
    void rejeitaSituacaoDesconhecidaAntesDeAcessarBanco() {
        assertThrows(IllegalArgumentException.class,
                () -> controller.gerarPorPeriodo(
                        LocalDate.now(), LocalDate.now(), "qualquer"));
    }
}
