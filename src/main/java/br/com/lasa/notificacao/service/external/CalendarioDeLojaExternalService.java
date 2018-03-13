package br.com.lasa.notificacao.service.external;

import br.com.lasa.notificacao.domain.document.Loja;
import br.com.lasa.notificacao.service.external.response.CalendarioDeLoja;

public interface CalendarioDeLojaExternalService {

    public CalendarioDeLoja buscarCalendarioDaSemanaDaLoja(String lojaId, String responsavelLoja);

    Loja montarEstrutura(CalendarioDeLoja calendarioDeLoja);
}
