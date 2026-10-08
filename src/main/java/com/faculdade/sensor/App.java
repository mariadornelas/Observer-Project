package com.faculdade.sensor;

public class App {

    public static void main(String[] args) {
        SensorIndustrial sensor = new SensorIndustrial("TEMPERATURA", 70.0, 90.0);

        PainelDisplay painel = new PainelDisplay();
        RegistroHistorico historico = new RegistroHistorico();
        AlarmeSeguranca alarme = new AlarmeSeguranca();

        sensor.adicionarObservador(painel);
        sensor.adicionarObservador(historico);
        sensor.adicionarObservador(alarme);

        System.out.println("=== Três observadores registrados ===");
        sensor.medir(50.0);
        sensor.medir(75.0);
        sensor.medir(95.0);

        System.out.println();
        System.out.println("=== Painel removido; os outros continuam recebendo ===");
        sensor.removerObservador(painel);
        sensor.medir(92.0);

        System.out.println();
        System.out.println("Observadores ativos: " + sensor.quantidadeDeObservadores());
        System.out.println("Leituras no histórico: " + historico.getLeituras().size());
        System.out.println("Alarmes disparados: " + alarme.getAlarmesDisparados().size());
        System.out.println("Última exibição do painel (parou de atualizar): " + painel.getUltimaExibicao());
    }
}
