package com.example.trabalho1pooii.strategy;

import java.time.LocalDate;

public interface PoliticaPrazo {

    LocalDate calcularDataLimite(LocalDate dataEmprestimo);

}