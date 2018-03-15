package br.com.lasa.notificacao.service;


import br.com.lasa.notificacao.domain.document.Notification;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificacaoService {

    boolean enviarNotificacao(Notification notification);

    List<Notification> buscarNotificacaoNaoProgramada(LocalDateTime scheduleTime);

    void releaseAllByHostname(String hostAddress);

    void setScheduleFor(ObjectId id, boolean schedule);

    void setScheduleFor(String id, boolean schedule);
}
