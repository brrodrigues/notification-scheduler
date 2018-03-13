package br.com.lasa.notificacao.service.external.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HorarioDiario {

    @JsonProperty(value = "dia_semana")
    private Integer diaSemana;
    @JsonProperty(value = "dia_ext_semana")
    private String diaExtSemana;
    @JsonProperty(value = "situacao")
    private String situacao;
    @JsonProperty(value = "status")
    private String status;
    @JsonProperty(value = "hora_abertura")
    private String horaAbertura;
    @JsonProperty(value = "hora_fechamento")
    private String horaFechamento;



}
