package br.com.biblioteca.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidacoesTest {
    @Test
    void validaCpfComDigitosVerificadores() {
        assertTrue(Validacoes.cpfValido("52998224725"));
        assertFalse(Validacoes.cpfValido("11111111111"));
        assertFalse(Validacoes.cpfValido("52998224724"));
    }

    @Test
    void normalizaCpfFormatado() {
        assertEquals("52998224725", Validacoes.normalizarCpf("529.982.247-25"));
        assertThrows(IllegalArgumentException.class,
                () -> Validacoes.normalizarCpf("123.456.789-00"));
    }

    @Test
    void validaIsbn10EIsbn13() {
        assertTrue(Validacoes.isbnValido("0306406152"));
        assertTrue(Validacoes.isbnValido("9780306406157"));
        assertFalse(Validacoes.isbnValido("9780306406158"));
        assertEquals("9780306406157", Validacoes.normalizarIsbn("978-0-306-40615-7"));
        assertThrows(IllegalArgumentException.class,
                () -> Validacoes.normalizarIsbn("9780306406158"));
    }

    @Test
    void validaEmailETelefoneOpcionais() {
        Validacoes.validarEmail("leitor@example.com");
        Validacoes.validarTelefone("(11) 99999-9999");
        Validacoes.validarEmail("");
        Validacoes.validarTelefone(null);
        assertThrows(IllegalArgumentException.class,
                () -> Validacoes.validarEmail("endereco-invalido"));
        assertThrows(IllegalArgumentException.class,
                () -> Validacoes.validarTelefone("123"));
    }
}
