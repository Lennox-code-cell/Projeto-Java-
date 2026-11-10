package br.com.biblioteca.controller;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class EmprestimoControllerTest {
    private final EmprestimoController controller = new EmprestimoController();

    @Test
    void rejeitaIdentificadoresInvalidosSemAcessarBanco() {
        assertThrows(IllegalArgumentException.class,
                () -> controller.registrarEmprestimo(0, 1, LocalDate.now(), LocalDate.now()));
        assertThrows(IllegalArgumentException.class,
                () -> controller.registrarEmprestimo(1, -1, LocalDate.now(), LocalDate.now()));
    }

    @Test
    void rejeitaDatasAusentesFuturasOuInvertidasSemAcessarBanco() {
        LocalDate hoje = LocalDate.now();
        assertThrows(IllegalArgumentException.class,
                () -> controller.registrarEmprestimo(1, 1, null, hoje));
        assertThrows(IllegalArgumentException.class,
                () -> controller.registrarEmprestimo(1, 1, hoje.plusDays(1), hoje.plusDays(2)));
        assertThrows(IllegalArgumentException.class,
                () -> controller.registrarEmprestimo(1, 1, hoje, hoje.minusDays(1)));
        assertThrows(IllegalArgumentException.class,
                () -> controller.registrarDevolucao(1, hoje.plusDays(1)));
    }
}
