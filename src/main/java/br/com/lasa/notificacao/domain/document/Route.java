package br.com.lasa.notificacao.domain.document;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class Route implements Serializable{

    private String name;
    private String url;

}
