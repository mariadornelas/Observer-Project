package com.faculdade.sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Subject (Concrete Subject) do padrão Observer.
 *
 * <p>Mantém a lista de observadores interessados e, a cada
 * {@link #medir(double)}, notifica todos eles, na ordem em que foram
 * registrados. O sensor não sabe quantos observadores existem nem o que
 * cada um faz com a leitura.</p>
 *
 * <p>A notificação percorre uma <b>cópia</b> da lista, então um observador
 * pode se remover (ou registrar outro) durante a própria notificação sem
 * causar {@code ConcurrentModificationException}. Nesse caso, a mudança
 * vale a partir da próxima medição.</p>
 */
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
