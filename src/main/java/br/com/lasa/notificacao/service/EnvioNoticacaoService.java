package br.com.lasa.notificacao.service;

import org.springframework.web.client.HttpStatusCodeException;

import java.util.Map;
import java.util.Set;

public interface EnvioNoticacaoService {

    void notificar(Map.Entry<String, Set<String>> notification) throws HttpStatusCodeException;

}
