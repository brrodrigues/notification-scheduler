package br.com.lasa.notificacao.service.external.response;

import br.com.lasa.notificacao.domain.document.Horario;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
public class CalendarioDeLoja {

    private List<Horario> horarios;
    private String lojaId;
    private String nomeLoja;
    private String nomeResponsavel;
    private Map<String, Object> metadata;

}
