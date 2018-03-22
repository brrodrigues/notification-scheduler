package br.com.lasa.notificacao.repository;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public interface NotificacaoRepositoryCustom {

    int setScheduleAndUuiAndHostnameForSpecificScheduleTime(LocalDateTime minute, boolean scheduled, String uuid, String hostname, int limit);

    int setNotificacaoFor(ObjectId objectId, boolean schedule);

    void releaseNotificacaoByHostname(String hostname);

    void addUser(String objectId, String numeroLoja);

    void removeUser(String channel, String numeroLoja);
}
