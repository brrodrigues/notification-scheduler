package br.com.lasa.notificacao.domain.document;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
public class Permission implements Serializable {

    private String id;
    private List<String> httpMethod;
    private List<Route> routes;

}
