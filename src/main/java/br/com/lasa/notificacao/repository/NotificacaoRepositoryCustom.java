package br.com.lasa.notificacao.repository;

public interface NotificacaoRepositoryCustom {

    int setScheduleAndUuiAndHostnameFor(boolean schedule, String uuid, String hostname, int limit);

    void releaseSchedulebyHostname(String hostname);
}
