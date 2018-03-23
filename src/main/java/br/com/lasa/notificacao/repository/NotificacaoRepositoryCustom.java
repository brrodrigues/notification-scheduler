package br.com.lasa.notificacao.repository;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public interface NotificacaoRepositoryCustom {

    Map<String, Set<String>> setScheduleAndUuiAndHostnameForSpecificScheduleTimeAndReturnNotificationMap(LocalDateTime minute, boolean scheduled, String uuid, String hostname, int limit);

    int setNotificacaoFor(ObjectId objectId, boolean schedule);

    void releaseNotificacaoByHostname(String hostname);

    void addUser(String objectId, String numeroLoja);

    void removeUser(String channel, String numeroLoja);
}
