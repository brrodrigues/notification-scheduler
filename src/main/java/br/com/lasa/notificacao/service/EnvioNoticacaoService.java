package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Notificacao;
import org.springframework.web.client.HttpStatusCodeException;

public interface EnvioNoticacaoService {
    void notificar(Notificacao notificacao) throws HttpStatusCodeException;
}
