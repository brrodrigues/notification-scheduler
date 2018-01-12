package br.com.lasa.notificacao.repository;

import org.bson.types.ObjectId;

import java.time.LocalTime;

public interface NotificacaoRepositoryCustom {

    int setScheduleAndUuiAndHostnameForSpecificScheduleTimeAfter(LocalTime minute, boolean scheduled, String uuid, String hostname, int limit);

    int setScheduleAndUuiAndHostnameForSpecificScheduleTimebefore(LocalTime minute, boolean scheduled, String uuid, String hostname, int limit);

    int setScheduleAndUuiAndHostnameForMinute(int minute, boolean schedule, String uuid, String hostname, int limit);

    int setNotificacaoFor(ObjectId objectId, boolean schedule);

    void releaseNotificacaoByHostname(String hostname);

    void addUser(String objectId, String numeroLoja);

    void removeUser(String channel, String numeroLoja);
}
