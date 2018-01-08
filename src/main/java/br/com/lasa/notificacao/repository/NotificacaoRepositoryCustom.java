package br.com.lasa.notificacao.repository;

import org.bson.types.ObjectId;

public interface NotificacaoRepositoryCustom {

    int setScheduleAndUuiAndHostnameFor(int minute, boolean schedule, String uuid, String hostname, int limit);

    int setScheduleFor(ObjectId objectId, boolean schedule);

    void releaseSchedulebyHostname(String hostname);

    void addUser(String objectId, String numeroLoja);

    void removeUser(String channel, String numeroLoja);
}
