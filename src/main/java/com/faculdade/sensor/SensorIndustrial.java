package com.faculdade.sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SensorIndustrial {

    private final String tipo;
    private final double limiteAlerta;
    private final double limiteCritico;
    private final List<ObservadorSensor> observadores = new ArrayList<>();

    public SensorIndustrial(String tipo, double limiteAlerta, double limiteCritico) {
        if (limiteAlerta > limiteCritico) {
            throw new IllegalArgumentException("limiteAlerta não pode ser maior que limiteCritico");
        }
        this.tipo = tipo;
        this.limiteAlerta = limiteAlerta;
        this.limiteCritico = limiteCritico;
    }

    public void adicionarObservador(ObservadorSensor observador) {
        Objects.requireNonNull(observador, "observador não pode ser nulo");
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public boolean removerObservador(ObservadorSensor observador) {
        return observadores.remove(observador);
    }

    public int quantidadeDeObservadores() {
        return observadores.size();
    }

    public String medir(double valorMedido) {
        String status = classificar(valorMedido);
        notificar(new LeituraSensor(tipo, valorMedido, status));
        return status;
    }

    public String getTipo() {
        return tipo;
    }

    private void notificar(LeituraSensor leitura) {
        for (ObservadorSensor observador : new ArrayList<>(observadores)) {
            observador.atualizar(leitura);
        }
    }

    private String classificar(double valorMedido) {
        if (valorMedido >= limiteCritico) {
            return "CRITICO";
        }
        if (valorMedido >= limiteAlerta) {
            return "ALERTA";
        }
        return "NORMAL";
    }
}
