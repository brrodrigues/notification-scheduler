package br.com.lasa.notificacao.domain.document;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class Feature implements Serializable{

    private String name;
    private String route;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
