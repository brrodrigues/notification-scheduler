package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Notificacao;
import org.bson.types.ObjectId;

import java.time.LocalTime;
import java.util.List;

public interface NotificacaoService {

    boolean enviarNotificacao(Notificacao notificacao);

    List<Notificacao> buscarNotificacaoNaoProgramada(LocalTime scheduleTime);

    List<Notificacao> buscarNotificacaoNaoProgramada(int minute);

    void releaseAllByHostname(String hostAddress);

    void setScheduleFor(ObjectId id, boolean schedule);

    void setScheduleFor(String id, boolean schedule);
}
