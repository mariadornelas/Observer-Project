package com.faculdade.sensor;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** O Subject: classificação, gerência da lista de observadores e regras de notificação. */
class SensorIndustrialTest {

    private final SensorIndustrial sensor = new SensorIndustrial("TEMPERATURA", 70.0, 90.0);

    @Test
    void deveClassificarAsTresFaixas() {
        assertEquals("NORMAL", sensor.medir(50.0));
        assertEquals("ALERTA", sensor.medir(75.0));
        assertEquals("CRITICO", sensor.medir(95.0));
    }

    @Test
    void naoDeveAceitarLimiteDeAlertaMaiorQueOCritico() {
        assertThrows(IllegalArgumentException.class, () -> new SensorIndustrial("TEMPERATURA", 90.0, 70.0));
    }

    @Test
    void medirSemNenhumObservadorDeveFuncionar() {
        assertEquals("NORMAL", sensor.medir(10.0));
    }

    @Test
    void deveNotificarTodosOsObservadoresAPartirDeUmaUnicaMedicao() {
        List<LeituraSensor> recebidasA = new ArrayList<>();
        List<LeituraSensor> recebidasB = new ArrayList<>();
        sensor.adicionarObservador(recebidasA::add);
        sensor.adicionarObservador(recebidasB::add);

        sensor.medir(75.0);

        assertEquals(1, recebidasA.size());
        assertEquals(1, recebidasB.size());
    }

    @Test
    void observadorDeveReceberTipoValorEStatusDaLeitura() {
        List<LeituraSensor> recebidas = new ArrayList<>();
        sensor.adicionarObservador(recebidas::add);

        sensor.medir(75.0);

        assertEquals(new LeituraSensor("TEMPERATURA", 75.0, "ALERTA"), recebidas.get(0));
    }

    @Test
    void deveNotificarNaOrdemDeRegistro() {
        List<String> ordem = new ArrayList<>();
        sensor.adicionarObservador(l -> ordem.add("primeiro"));
        sensor.adicionarObservador(l -> ordem.add("segundo"));
        sensor.adicionarObservador(l -> ordem.add("terceiro"));

        sensor.medir(50.0);

        assertEquals(List.of("primeiro", "segundo", "terceiro"), ordem);
    }

    @Test
    void observadorRemovidoNaoDeveMaisReceberNotificacoes() {
        List<LeituraSensor> recebidas = new ArrayList<>();
        ObservadorSensor observador = recebidas::add;
        sensor.adicionarObservador(observador);
        sensor.medir(50.0);

        assertTrue(sensor.removerObservador(observador));
        sensor.medir(60.0);

        assertEquals(1, recebidas.size());
    }

    @Test
    void removerObservadorNaoRegistradoDeveDevolverFalse() {
        assertFalse(sensor.removerObservador(l -> { }));
    }

    @Test
    void mesmoObservadorRegistradoDuasVezesDeveSerNotificadoUmaSoVez() {
        List<LeituraSensor> recebidas = new ArrayList<>();
        ObservadorSensor observador = recebidas::add;
        sensor.adicionarObservador(observador);
        sensor.adicionarObservador(observador);

        sensor.medir(50.0);

        assertEquals(1, sensor.quantidadeDeObservadores());
        assertEquals(1, recebidas.size());
    }

    @Test
    void naoDeveAceitarObservadorNulo() {
        assertThrows(NullPointerException.class, () -> sensor.adicionarObservador(null));
    }

    @Test
    void observadorPodeSeRemoverDuranteANotificacaoSemQuebrarOsDemais() {
        int[] chamadasDoAutoRemovivel = { 0 };
        List<LeituraSensor> recebidasPeloOutro = new ArrayList<>();

        sensor.adicionarObservador(new ObservadorSensor() {
            @Override
            public void atualizar(LeituraSensor leitura) {
                chamadasDoAutoRemovivel[0]++;
                sensor.removerObservador(this);
            }
        });
        sensor.adicionarObservador(recebidasPeloOutro::add);

        assertDoesNotThrow(() -> sensor.medir(50.0));
        sensor.medir(60.0);

        assertEquals(1, chamadasDoAutoRemovivel[0]);
        assertEquals(2, recebidasPeloOutro.size());
    }
}
