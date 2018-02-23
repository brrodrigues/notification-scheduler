package br.com.lasa.notificacao.service.external;

import br.com.lasa.notificacao.service.external.response.InformacaoLoja;

import java.util.Collection;

public interface CalendarioDeLojaService {

    Collection<InformacaoLoja> get(String loja);

}
