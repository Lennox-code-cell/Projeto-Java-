package br.com.biblioteca;

/**
 * Classe principal que inicia a aplicação e abre a tela inicial do sistema.
 */

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf;

import br.com.biblioteca.view.TelaPrincipal;
import br.com.biblioteca.dao.EmprestimoDAO;

import java.awt.Color;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final ScheduledExecutorService SINCRONIZADOR_EMPRESTIMOS =
            Executors.newSingleThreadScheduledExecutor(runnable -> {
                Thread thread = new Thread(runnable, "sincronizador-emprestimos");
                thread.setDaemon(true);
                return thread;
            });

    public static void main(String[] args) {
        if (!FlatLightLaf.setup()) {
            throw new IllegalStateException("Não foi possível iniciar o tema da interface.");
        }
        Runtime.getRuntime().addShutdownHook(new Thread(
                SINCRONIZADOR_EMPRESTIMOS::shutdownNow,
                "encerramento-sincronizador-emprestimos"
        ));
        UIManager.put("Component.arc", 10);
        UIManager.put("Button.arc", 8);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("Table.rowHeight", 30);
        UIManager.put("Table.alternateRowColor", new Color(245, 247, 250));
        UIManager.put("Table.showHorizontalLines", true);
        SINCRONIZADOR_EMPRESTIMOS.execute(() -> {
            sincronizarEAgendar();
            SwingUtilities.invokeLater(Main::mostrarTela);
        });
    }

    private static void mostrarTela() {
        TelaPrincipal tela = new TelaPrincipal();
        tela.setVisible(true);
    }

    private static void sincronizarEAgendar() {
        try {
            sincronizarSituacoes();
        } finally {
            agendarProximaSincronizacao();
        }
    }

    private static void agendarProximaSincronizacao() {
        if (SINCRONIZADOR_EMPRESTIMOS.isShutdown()) {
            return;
        }

        LocalDateTime proximaMeiaNoite = LocalDate.now().plusDays(1).atStartOfDay();
        long atraso = Duration.between(LocalDateTime.now(), proximaMeiaNoite).toMillis();
        SINCRONIZADOR_EMPRESTIMOS.schedule(
                Main::sincronizarEAgendar,
                Math.max(atraso, 0),
                TimeUnit.MILLISECONDS
        );
    }

    private static void sincronizarSituacoes() {
        try {
            new EmprestimoDAO().sincronizarSituacoes();
        } catch (Exception ex) {
            LOGGER.log(
                    Level.SEVERE,
                    "Não foi possível sincronizar empréstimos e exemplares. "
                            + "Verifique DB_URL, DB_USER, DB_PASSWORD e o servidor MySQL."
            );
        }
    }
}
