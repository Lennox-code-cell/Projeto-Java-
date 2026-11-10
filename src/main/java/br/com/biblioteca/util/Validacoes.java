package br.com.biblioteca.util;

import java.util.Locale;

public final class Validacoes {
    private Validacoes() {
    }

    public static String normalizarCpf(String cpf) {
        String normalizado = cpf == null ? "" : cpf.replaceAll("[.\\s-]", "");
        if (!cpfValido(normalizado)) {
            throw new IllegalArgumentException("Informe um CPF válido.");
        }
        return normalizado;
    }

    public static boolean cpfValido(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}") || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        int primeiroDigito = calcularDigitoCpf(cpf, 9);
        int segundoDigito = calcularDigitoCpf(cpf, 10);
        return cpf.charAt(9) - '0' == primeiroDigito
                && cpf.charAt(10) - '0' == segundoDigito;
    }

    public static void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            return;
        }
        String valor = email.trim();
        if (valor.length() > 120
                || !valor.matches("^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@"
                + "[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?"
                + "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)+$")) {
            throw new IllegalArgumentException("Informe um e-mail válido.");
        }
    }

    public static void validarTelefone(String telefone) {
        if (telefone == null || telefone.isBlank()) {
            return;
        }
        if (!telefone.matches("[0-9+() .-]+")) {
            throw new IllegalArgumentException("Informe um telefone com DDD válido.");
        }
        String digitos = telefone.replaceAll("\\D", "");
        if (!digitos.matches("\\d{10,13}")) {
            throw new IllegalArgumentException("Informe um telefone com DDD válido.");
        }
    }

    public static String normalizarIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            return null;
        }
        String normalizado = isbn.replaceAll("[-\\s]", "").toUpperCase(Locale.ROOT);
        if (!isbnValido(normalizado)) {
            throw new IllegalArgumentException("Informe um ISBN-10 ou ISBN-13 válido.");
        }
        return normalizado;
    }

    public static boolean isbnValido(String isbn) {
        if (isbn == null) {
            return false;
        }
        if (isbn.matches("\\d{13}")) {
            int soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += (isbn.charAt(i) - '0') * (i % 2 == 0 ? 1 : 3);
            }
            return (10 - soma % 10) % 10 == isbn.charAt(12) - '0';
        }
        if (isbn.matches("\\d{9}[\\dX]")) {
            int soma = 0;
            for (int i = 0; i < 10; i++) {
                int digito = isbn.charAt(i) == 'X' ? 10 : isbn.charAt(i) - '0';
                soma += digito * (10 - i);
            }
            return soma % 11 == 0;
        }
        return false;
    }

    private static int calcularDigitoCpf(String cpf, int quantidade) {
        int soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += (cpf.charAt(i) - '0') * (quantidade + 1 - i);
        }
        int resto = soma * 10 % 11;
        return resto == 10 ? 0 : resto;
    }
}
