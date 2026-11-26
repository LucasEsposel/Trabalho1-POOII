package com.example.trabalho1pooii.strategy;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import com.example.trabalho1pooii.strategy.PrazoEstendido;
import com.example.trabalho1pooii.strategy.PrazoPadrao;

import static org.junit.jupiter.api.Assertions.*;

class PoliticaPrazoTest {

    @Test
    void deveCalcularPrazoPadraoDeSeteDias() {

        PrazoPadrao prazo = new PrazoPadrao();

        LocalDate dataEmprestimo = LocalDate.of(2026, 11, 1);

        LocalDate resultado = prazo.calcularDataLimite(dataEmprestimo);

        LocalDate esperado = LocalDate.of(2026, 11, 8);

        assertEquals(
                esperado,
                resultado,
                "A data limite deveria ser sete dias após o empréstimo."
        );
    }

    @Test
    void deveCalcularPrazoEstendidoDeQuinzeDias() {

        PrazoEstendido prazo = new PrazoEstendido();

        LocalDate dataEmprestimo = LocalDate.of(2026, 11, 1);

        LocalDate resultado = prazo.calcularDataLimite(dataEmprestimo);

        LocalDate esperado = LocalDate.of(2026, 11, 16);

        assertEquals(
                esperado,
                resultado,
                "A data limite deveria ser quinze dias após o empréstimo."
        );
    }

    @Test
    void deveRetornarDatasLimiteDiferentesParaCadaEstrategia() {

        LocalDate dataEmprestimo = LocalDate.of(2026, 11, 1);

        PrazoPadrao prazoPadrao = new PrazoPadrao();
        PrazoEstendido prazoEstendido = new PrazoEstendido();

        LocalDate dataPadrao = prazoPadrao.calcularDataLimite(dataEmprestimo);
        LocalDate dataEstendida = prazoEstendido.calcularDataLimite(dataEmprestimo);

        assertNotEquals(
                dataPadrao,
                dataEstendida,
                "As estratégias deveriam produzir datas limite diferentes."
        );
    }
}