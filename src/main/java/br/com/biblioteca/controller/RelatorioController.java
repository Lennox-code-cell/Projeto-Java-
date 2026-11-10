package br.com.biblioteca.controller;

import br.com.biblioteca.dao.RelatorioDAO;
import br.com.biblioteca.model.EmprestimoRelatorio;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class RelatorioController {
    private static final Set<String> SITUACOES = Set.of("todos", "ativo", "atrasado", "devolvido");

    private final RelatorioDAO relatorioDAO = new RelatorioDAO();

    public List<EmprestimoRelatorio> gerarPorPeriodo(
            LocalDate inicio,
            LocalDate fim,
            String situacao
    ) throws SQLException {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("Informe as datas inicial e final.");
        }
        if (fim.isBefore(inicio)) {
            throw new IllegalArgumentException("A data final não pode ser anterior à data inicial.");
        }
        String filtro = situacao == null ? "" : situacao.trim().toLowerCase(Locale.ROOT);
        if (!SITUACOES.contains(filtro)) {
            throw new IllegalArgumentException("Selecione uma situação válida para o relatório.");
        }
        return relatorioDAO.gerarRelatorioPorPeriodo(inicio, fim, filtro);
    }
}
