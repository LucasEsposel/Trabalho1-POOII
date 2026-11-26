package com.example.trabalho1pooii.strategy;

import java.time.LocalDate;

public class PrazoEstendido implements PoliticaPrazo {

    @Override
    public LocalDate calcularDataLimite(LocalDate dataEmprestimo) {
        return dataEmprestimo.plusDays(15);
    }
}