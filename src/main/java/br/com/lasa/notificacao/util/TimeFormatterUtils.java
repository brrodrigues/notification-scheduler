package br.com.lasa.notificacao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimeFormatterUtils {

    private final Logger LOGGER = LoggerFactory.getLogger(TimeFormatterUtils.class);

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

}
