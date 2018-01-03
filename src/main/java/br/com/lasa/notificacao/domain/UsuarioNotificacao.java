package br.com.lasa.notificacao.domain;


import java.util.List;

public interface UsuarioNotificacao {
    String getLogin();

    String getStoreId();

    String getHierarchy();

    List<Object> getPerfis();

    void setLogin(String login);

    void setStoreId(String storeId);

    void setHierarchy(String hierarchy);

    void setPerfis(List<Object> perfis);
}
