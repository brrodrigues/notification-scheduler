package br.com.lasa.notificacao.service;


import br.com.lasa.notificacao.domain.document.Notification;
import org.bson.types.ObjectId;

import java.time.LocalTime;
import java.util.List;

public interface NotificacaoService {

    boolean enviarNotificacao(Notification notification);

    List<Notification> buscarNotificacaoNaoProgramada(LocalTime scheduleTime);

    List<Notification> buscarNotificacaoNaoProgramada(int minute);

    void releaseAllByHostname(String hostAddress);

    void setScheduleFor(ObjectId id, boolean schedule);

    void setScheduleFor(String id, boolean schedule);
}
