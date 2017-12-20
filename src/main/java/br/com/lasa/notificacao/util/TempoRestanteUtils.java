package br.com.lasa.notificacao.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TempoRestanteUtils {

    public static long get(Date dataInicio, Date dataFinal){

        LocalDate dataAtual = LocalDate.from(dataInicio.toInstant());

        LocalDate dataSchedule = LocalDate.from(dataFinal.toInstant());

        long duracao = ChronoUnit.SECONDS.between(dataAtual, dataSchedule);

        return duracao;
    }
}
