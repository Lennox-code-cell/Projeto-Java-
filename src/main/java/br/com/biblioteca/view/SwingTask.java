package br.com.biblioteca.view;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.awt.Component;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

final class SwingTask {
    private SwingTask() {
    }

    @FunctionalInterface
    interface SqlOperation {
        void run() throws Exception;
    }

    static <T> void run(
            Component parent,
            String errorTitle,
            Callable<T> task,
            Consumer<T> onSuccess,
            Consumer<Exception> onError
    ) {
        new SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() throws Exception {
                return task.call();
            }

            @Override
            protected void done() {
                try {
                    onSuccess.accept(get());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    onError.accept(ex);
                } catch (ExecutionException ex) {
                    Throwable cause = ex.getCause();
                    onError.accept(cause instanceof Exception exception
                            ? exception
                            : new RuntimeException("Falha inesperada na operação.", cause));
                }
            }
        }.execute();
    }

    static void showError(Component parent, String title, Exception error) {
        String message = error.getMessage() == null
                ? "Não foi possível concluir a operação."
                : error.getMessage();
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
