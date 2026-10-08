package com.faculdade.sensor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Cada observador concreto decide, sozinho, o que fazer com a mesma notificação. */
class ObservadoresConcretosTest {

    private final LeituraSensor normal = new LeituraSensor("TEMPERATURA", 50.0, "NORMAL");
    private final LeituraSensor alerta = new LeituraSensor("TEMPERATURA", 75.0, "ALERTA");
    private final LeituraSensor critica = new LeituraSensor("PRESSAO", 15.0, "CRITICO");

    // ---- PainelDisplay: mostra tudo ----

    @Test
    void painelNaoDeveTerExibicaoAntesDaPrimeiraLeitura() {
        assertNull(new PainelDisplay().getUltimaExibicao());
    }

    @Test
    void painelDeveExibirTambemLeiturasNormais() {
        PainelDisplay painel = new PainelDisplay();

        painel.atualizar(normal);

        assertTrue(painel.getUltimaExibicao().contains("TEMPERATURA"));
        assertTrue(painel.getUltimaExibicao().contains("NORMAL"));
    }

    @Test
    void painelDeveManterApenasAUltimaExibicao() {
        PainelDisplay painel = new PainelDisplay();

        painel.atualizar(normal);
        painel.atualizar(critica);

        assertTrue(painel.getUltimaExibicao().contains("PRESSAO"));
        assertTrue(painel.getUltimaExibicao().contains("CRITICO"));
    }

    // ---- RegistroHistorico: guarda tudo, em ordem ----

    @Test
    void historicoDeveGuardarTodasAsLeiturasEmOrdem() {
        RegistroHistorico historico = new RegistroHistorico();

        historico.atualizar(normal);
        historico.atualizar(alerta);
        historico.atualizar(critica);

        assertEquals(java.util.List.of(normal, alerta, critica), historico.getLeituras());
    }

    @Test
    void historicoDevolvidoDeveSerSomenteLeitura() {
        RegistroHistorico historico = new RegistroHistorico();
        historico.atualizar(normal);

        assertThrows(UnsupportedOperationException.class, () -> historico.getLeituras().add(alerta));
    }

    // ---- AlarmeSeguranca: só reage ao que não é NORMAL ----

    @Test
    void alarmeNaoDeveDispararParaLeituraNormal() {
        AlarmeSeguranca alarme = new AlarmeSeguranca();

        alarme.atualizar(normal);

        assertTrue(alarme.getAlarmesDisparados().isEmpty());
    }

    @Test
    void alarmeDeveDispararParaAlertaECritico() {
        AlarmeSeguranca alarme = new AlarmeSeguranca();

        alarme.atualizar(alerta);
        alarme.atualizar(critica);

        assertEquals(2, alarme.getAlarmesDisparados().size());
        assertTrue(alarme.getAlarmesDisparados().get(0).contains("ALERTA"));
        assertTrue(alarme.getAlarmesDisparados().get(1).contains("CRITICO"));
        assertTrue(alarme.getAlarmesDisparados().get(1).contains("PRESSAO"));
    }

    @Test
    void alarmesDevolvidosDevemSerSomenteLeitura() {
        AlarmeSeguranca alarme = new AlarmeSeguranca();
        alarme.atualizar(alerta);

        assertThrows(UnsupportedOperationException.class, () -> alarme.getAlarmesDisparados().add("forjado"));
    }
}
