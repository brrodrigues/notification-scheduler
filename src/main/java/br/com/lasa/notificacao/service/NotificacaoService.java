package br.com.lasa.notificacao.service;


import br.com.lasa.notificacao.domain.document.Notification;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public interface NotificacaoService {

    Page<Notification> findAllDiretoria(Pageable page);

    void enviarNotificacao(Notification notification, Map<String,Object> metadata);

    boolean enviarNotificacao(Map.Entry<String, Set<String>> notification);

    Map<String, Set<String>> buscarMapaDeNotificacaoNaoProgramada(LocalDateTime scheduleTime);

    void releaseAllByHostname(String hostAddress);

    void setScheduleFor(ObjectId id, boolean schedule);

    void setScheduleFor(String id, boolean schedule);

    Notification get(String id);
}
