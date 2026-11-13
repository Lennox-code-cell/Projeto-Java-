package br.com.biblioteca.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SituacaoEmprestimoTest {
    private final LocalDate hoje = LocalDate.of(2026, 11, 12);

    @Test
    void prazoHojeOuFuturoPermaneceAtivo() {
        assertEquals(SituacaoEmprestimo.ATIVO,
                SituacaoEmprestimo.calcular(hoje, null, hoje));
        assertEquals(SituacaoEmprestimo.ATIVO,
                SituacaoEmprestimo.calcular(hoje.plusDays(1), null, hoje));
    }

    @Test
    void prazoVencidoSemDevolucaoFicaAtrasado() {
        assertEquals(SituacaoEmprestimo.ATRASADO,
                SituacaoEmprestimo.calcular(hoje.minusDays(1), null, hoje));
    }

    @Test
    void devolucaoPrevaleceMesmoQuandoOEmprestimoEstavaAtrasado() {
        assertEquals(SituacaoEmprestimo.DEVOLVIDO,
                SituacaoEmprestimo.calcular(
                        hoje.minusDays(2), hoje.minusDays(1), hoje
                ));
    }

    @Test
    void exigeDatasDeReferencia() {
        assertThrows(IllegalArgumentException.class,
                () -> SituacaoEmprestimo.calcular(null, null, hoje));
        assertThrows(IllegalArgumentException.class,
                () -> SituacaoEmprestimo.calcular(hoje, null, null));
    }
}
