package br.com.lasa.notificacao.service.external.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class InformacaoFeriadoLoja {

    private String loja;
    private String centro;
    @JsonProperty(value = "nome_loja")
    private String nomeLoja;
    @JsonProperty(value = "nome_comb")
    private String nomeCombinada;
    @JsonProperty(value = "regiao")
    private String regiao;
    @JsonProperty(value = "nome_regiao")
    private String nomeRegiao;
    @JsonProperty(value = "id_distrito")
    private String distritoId;
    @JsonProperty(value = "desc_distrito")
    private String descricaoDistrito;
    @JsonProperty(value = "hora_abertura")
    private String horaAbertura;
    @JsonProperty(value = "hora_fechamento")
    private String horaFechamento;
    @JsonProperty(value = "data_alterada")
    private String dataAlterada;
    private String motivo;
    private String situacao;
    private String status;
    @JsonProperty(value = "sem_horario_verao")
    private String semHorarioVerao;
    @JsonProperty(value = "com_horario_verao")
    private String comHorarioVerao;
    @JsonProperty(value = "horario_local")
    private String horarioLocal;


}
