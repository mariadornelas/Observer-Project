package com.faculdade.sensor;

/**
 * Dado publicado pelo sensor a cada medição. É imutável, então todos os
 * observadores recebem exatamente a mesma informação e nenhum deles pode
 * alterá-la para os demais.
 */
public record LeituraSensor(String tipo, double valor, String status) {
}
