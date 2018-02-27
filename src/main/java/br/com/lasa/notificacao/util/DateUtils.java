package br.com.lasa.notificacao.util;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {

    public static LocalTime toLocalTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.of("UTC") )
                .toLocalTime().truncatedTo(ChronoUnit.MINUTES);
    }

    public static LocalDate toLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.of("UTC") )
                .toLocalDate();
    }

    public static LocalTime toLocalTimeViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.of("UTC") )
                .toLocalTime().truncatedTo(ChronoUnit.MINUTES);
    }

    public static Date toDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert
                        .atZone(ZoneId.of("UTC") )
                        .toInstant());
    }

}
