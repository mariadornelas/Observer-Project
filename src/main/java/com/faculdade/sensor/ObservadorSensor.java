package com.faculdade.sensor;

/**
 * Observer do padrão: qualquer objeto interessado nas leituras de um sensor
 * implementa esta interface. O sensor só conhece este contrato, nunca as
 * classes concretas dos observadores.
 *
 * <p>É uma interface funcional, então um observador simples pode ser
 * escrito como lambda.</p>
 */
@FunctionalInterface
public interface ObservadorSensor {
    void atualizar(LeituraSensor leitura);
}
