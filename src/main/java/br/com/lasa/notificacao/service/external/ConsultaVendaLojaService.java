package br.com.lasa.notificacao.service.external;

import java.time.LocalDateTime;

public interface ConsultaVendaLojaService {

    boolean possuiVendaNoPeriodo(String loja, LocalDateTime dataHoraReferencia, Integer periodoEmMinuto);
}
