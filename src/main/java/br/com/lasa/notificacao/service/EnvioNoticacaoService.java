package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.document.Notification;
import org.springframework.web.client.HttpStatusCodeException;

public interface EnvioNoticacaoService {
    void notificar(Notification notification) throws HttpStatusCodeException;
}
