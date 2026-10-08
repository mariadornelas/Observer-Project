package com.faculdade.sensor;

/** Concrete Observer 1: mostra toda leitura recebida, sem filtrar. */
public class PainelDisplay implements ObservadorSensor {

    private String ultimaExibicao;

    @Override
    public void atualizar(LeituraSensor leitura) {
        ultimaExibicao = String.format("[PAINEL] %s = %.1f (%s)",
                leitura.tipo(), leitura.valor(), leitura.status());
        System.out.println(ultimaExibicao);
    }

    /** Última linha exibida, ou {@code null} se ainda não recebeu nenhuma leitura. */
    public String getUltimaExibicao() {
        return ultimaExibicao;
    }
}
