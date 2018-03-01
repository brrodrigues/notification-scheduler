package br.com.lasa.notificacao.service.external;

import java.time.LocalDateTime;

public interface ConsultaVendaLojaService {

    /**
     * Valida se a aplicacao enviara a notificacao para a loja 'X' caso o periodo + data de venda seja menor que a data de referencia
     * @param loja Numero da loja
     * @param dataHoraReferencia Horario de Referencia para validar
     * @param periodoEmMinuto Tempo de referencia com o
     * @return true ou false
     */
    boolean notificarLojaPorVendaForaDoPeriodo(String loja, LocalDateTime dataHoraReferencia, Integer periodoEmMinuto);
}
