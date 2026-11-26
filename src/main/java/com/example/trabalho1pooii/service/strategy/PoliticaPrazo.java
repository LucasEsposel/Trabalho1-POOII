package com.example.trabalho1pooii.service.strategy;

import java.time.LocalDate;

public interface PoliticaPrazo {

    LocalDate calcularDataLimite(LocalDate dataEmprestimo);

}