package com.faculdade.sensor;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * O que caracteriza o Observer: o sensor e seus observadores ficam
 * desacoplados, e a lista de interessados pode mudar em tempo de execução.
 */
class SensorObserverIntegracaoTest {

    @Test
    void sensorDeveFuncionarComObservadoresQueNaoConhece() {
        // Lambdas anônimos: o sensor nunca viu estas "classes" em tempo de compilação.
        SensorIndustrial sensor = new SensorIndustrial("PRESSAO", 8.0, 12.0);
        List<String> log = new ArrayList<>();
        sensor.adicionarObservador(l -> log.add("A:" + l.status()));
        sensor.adicionarObservador(l -> log.add("B:" + l.valor()));

        sensor.medir(13.0);

        assertEquals(List.of("A:CRITICO", "B:13.0"), log);
    }

    @Test
    void observadorAdicionadoDepoisSoVeAsLeiturasSeguintes() {
        SensorIndustrial sensor = new SensorIndustrial("TEMPERATURA", 70.0, 90.0);
        RegistroHistorico historico = new RegistroHistorico();

        sensor.medir(50.0);
        sensor.adicionarObservador(historico);
        sensor.medir(60.0);
        sensor.medir(95.0);

        assertEquals(2, historico.getLeituras().size());
        assertEquals(60.0, historico.getLeituras().get(0).valor());
    }

    @Test
    void umMesmoObservadorPodeAcompanharVariosSensores() {
        SensorIndustrial temperatura = new SensorIndustrial("TEMPERATURA", 70.0, 90.0);
        SensorIndustrial pressao = new SensorIndustrial("PRESSAO", 8.0, 12.0);
        RegistroHistorico historico = new RegistroHistorico();
        temperatura.adicionarObservador(historico);
        pressao.adicionarObservador(historico);

        temperatura.medir(50.0);
        pressao.medir(13.0);

        assertEquals(2, historico.getLeituras().size());
        assertEquals("TEMPERATURA", historico.getLeituras().get(0).tipo());
        assertEquals("PRESSAO", historico.getLeituras().get(1).tipo());
    }

    @Test
    void cenarioCompletoComOsTresObservadoresReais() {
        SensorIndustrial sensor = new SensorIndustrial("TEMPERATURA", 70.0, 90.0);
        PainelDisplay painel = new PainelDisplay();
        RegistroHistorico historico = new RegistroHistorico();
        AlarmeSeguranca alarme = new AlarmeSeguranca();
        sensor.adicionarObservador(painel);
        sensor.adicionarObservador(historico);
        sensor.adicionarObservador(alarme);

        sensor.medir(50.0);
        sensor.medir(75.0);
        sensor.medir(95.0);

        assertEquals(3, historico.getLeituras().size());      // guardou tudo
        assertEquals(2, alarme.getAlarmesDisparados().size()); // só ALERTA e CRITICO
        assertTrue(painel.getUltimaExibicao().contains("CRITICO")); // mostra a última
    }

    @Test
    void removerUmObservadorNaoDeveAfetarOsOutros() {
        SensorIndustrial sensor = new SensorIndustrial("TEMPERATURA", 70.0, 90.0);
        PainelDisplay painel = new PainelDisplay();
        RegistroHistorico historico = new RegistroHistorico();
        sensor.adicionarObservador(painel);
        sensor.adicionarObservador(historico);

        sensor.medir(50.0);
        sensor.removerObservador(painel);
        sensor.medir(95.0);

        assertEquals(2, historico.getLeituras().size());
        assertTrue(painel.getUltimaExibicao().contains("NORMAL")); // parou na leitura anterior
    }
}
