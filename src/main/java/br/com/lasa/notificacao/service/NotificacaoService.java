package br.com.lasa.notificacao.service;


import br.com.lasa.notificacao.domain.document.Notification;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public interface NotificacaoService {

    void enviarNotificacao(Notification notification);

    boolean enviarNotificacao(Map.Entry<String, Set<String>> notification);

    Map<String, Set<String>> buscarMapaDeNotificacaoNaoProgramada(LocalDateTime scheduleTime);

    void releaseAllByHostname(String hostAddress);

    void setScheduleFor(ObjectId id, boolean schedule);

    void setScheduleFor(String id, boolean schedule);

    Notification get(String id);
}
