package com.faculdade.sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlarmeSeguranca implements ObservadorSensor {

    private final List<String> alarmesDisparados = new ArrayList<>();

    @Override
    public void atualizar(LeituraSensor leitura) {
        if ("NORMAL".equals(leitura.status())) {
            return;
        }
        String alarme = String.format("ALARME [%s] %s: valor=%.1f",
                leitura.status(), leitura.tipo(), leitura.valor());
        System.out.println(alarme);
        alarmesDisparados.add(alarme);
    }

    public List<String> getAlarmesDisparados() {
        return Collections.unmodifiableList(alarmesDisparados);
    }
}
