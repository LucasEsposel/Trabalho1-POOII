package com.example.trabalho1pooii.strategy;

import java.time.LocalDate;

public class PrazoPadrao implements PoliticaPrazo {

    @Override
    public LocalDate calcularDataLimite(LocalDate dataEmprestimo) {
        return dataEmprestimo.plusDays(7);
    }
}