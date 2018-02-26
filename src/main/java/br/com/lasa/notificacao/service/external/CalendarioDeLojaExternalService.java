package br.com.lasa.notificacao.service.external;

import br.com.lasa.notificacao.domain.Horario;
import br.com.lasa.notificacao.service.external.response.InformacaoLoja;

import java.util.Collection;
import java.util.List;

public interface CalendarioDeLojaExternalService {

    Collection<InformacaoLoja> get(String loja);

    List<Horario> montarQuadroDeHorario(Collection<InformacaoLoja> informacaoLojas);

}
