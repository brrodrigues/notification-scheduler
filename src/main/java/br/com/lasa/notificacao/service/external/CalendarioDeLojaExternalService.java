package br.com.lasa.notificacao.service.external;

import br.com.lasa.notificacao.domain.document.Loja;
import br.com.lasa.notificacao.service.external.response.CalendarioDeLoja;
import br.com.lasa.notificacao.service.external.response.InformacaoFeriadoLoja;
import br.com.lasa.notificacao.service.external.response.InformacaoLoja;

import java.util.Collection;
import java.util.List;

public interface CalendarioDeLojaExternalService {

    public List<InformacaoLoja> buscarCalendarioDaSemanaDeTodasLoja();

    public CalendarioDeLoja buscarCalendarioFeriadoDaSemanaDaLoja(String lojaId, String responsavelLoja);

    Collection<InformacaoFeriadoLoja> buscarCalendarioFeriadoDaSemanaDaLoja(InformacaoLoja informacaoLoja);

    Loja toLoja(CalendarioDeLoja calendarioDeLoja);

    CalendarioDeLoja montarCalendario(InformacaoLoja informacaoLoja, Collection<InformacaoFeriadoLoja> informacaoLojaCollectionEntry);
}
