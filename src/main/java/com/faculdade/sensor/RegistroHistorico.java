package com.faculdade.sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegistroHistorico implements ObservadorSensor {

    private final List<LeituraSensor> leituras = new ArrayList<>();

    @Override
    public void atualizar(LeituraSensor leitura) {
        leituras.add(leitura);
    }

    public List<LeituraSensor> getLeituras() {
        return Collections.unmodifiableList(leituras);
    }
}
