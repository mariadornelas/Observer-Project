package com.faculdade.sensor;

@FunctionalInterface
public interface ObservadorSensor {
    void atualizar(LeituraSensor leitura);
}
