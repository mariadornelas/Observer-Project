package com.faculdade.sensor;

public class PainelDisplay implements ObservadorSensor {

    private String ultimaExibicao;

    @Override
    public void atualizar(LeituraSensor leitura) {
        ultimaExibicao = String.format("[PAINEL] %s = %.1f (%s)",
                leitura.tipo(), leitura.valor(), leitura.status());
        System.out.println(ultimaExibicao);
    }

    public String getUltimaExibicao() {
        return ultimaExibicao;
    }
}
