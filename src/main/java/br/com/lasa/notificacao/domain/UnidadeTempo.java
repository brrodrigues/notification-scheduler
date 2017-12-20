package br.com.lasa.notificacao.domain;

import java.util.concurrent.TimeUnit;

public enum UnidadeTempo {

    DIARIO(TimeUnit.DAYS), SEGUNDO(TimeUnit.SECONDS), MINUTO(TimeUnit.MINUTES), HORA(TimeUnit.HOURS);

    private TimeUnit timeUnit;

    UnidadeTempo(TimeUnit unit) {
        this.timeUnit = unit;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
