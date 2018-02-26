package br.com.lasa.notificacao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeFormatterUtils {

    private final Logger LOGGER = LoggerFactory.getLogger(DateTimeFormatterUtils.class);

    /**
     * Converte o horario em formato string para LocalTime
     * @param time
     * @return
     */
    public LocalTime toTime(String time) {
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(" Converting {} to format HH:mm:ss", time);
        }

        LocalTime parse = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));

        return parse;
    }

    /**
     * Converte o horario em formato string para LocalTime
     * @param yyyyMMdd Formato esperado yyyy-MM-dd
     * @return
     */
    public LocalDate toDate(String yyyyMMdd) {
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(" Converting {} to date format", yyyyMMdd);
        }

        LocalDate parse = LocalDate.parse(yyyyMMdd, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return parse;
    }

}
