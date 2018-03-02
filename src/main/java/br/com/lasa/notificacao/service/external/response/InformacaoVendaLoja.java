package br.com.lasa.notificacao.service.external.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@ToString
public class InformacaoVendaLoja {

    @JsonProperty(value = "centro")
    private String centro;
    @JsonProperty(value = "regiao")
    private String regiao;
    @JsonProperty(value = "nome_regiao")
    private String nomeRegiao;
    @JsonProperty(value = "uf")
    private String uf;
    @JsonProperty(value = "nome")
    private String nome;
    @JsonProperty(value = "loja")
    private Integer loja;
    @JsonProperty(value = "desc_distrito")
    private String descricaoDistrito;
    @JsonProperty(value = "abertura")
    private String horarioAbertura;
    @JsonProperty(value = "horario_fechamento")
    private String horarioFechamento;
    @JsonProperty(value = "diferenca")
    private String diferenca;
    @JsonProperty(value = "ultima_atualicao")
    private String ultimaAtualizacao;
    @JsonProperty(value = "tipo_pdv")
    private String tipoPDV;
    @JsonProperty(value = "horario_local")
    private String horarioLocal;

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getNomeRegiao() {
        return nomeRegiao;
    }

    public void setNomeRegiao(String nomeRegiao) {
        this.nomeRegiao = nomeRegiao;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getLoja() {
        return loja;
    }

    public void setLoja(Integer loja) {
        this.loja = loja;
    }

    public String getDescricaoDistrito() {
        return descricaoDistrito;
    }

    public void setDescricaoDistrito(String descricaoDistrito) {
        this.descricaoDistrito = descricaoDistrito;
    }

    public String getHorarioAbertura() {
        return horarioAbertura;
    }

    public void setHorarioAbertura(String horarioAbertura) {
        this.horarioAbertura = horarioAbertura;
    }

    public String getHorarioFechamento() {
        return horarioFechamento;
    }

    public void setHorarioFechamento(String horarioFechamento) {
        this.horarioFechamento = horarioFechamento;
    }

    public String getDiferenca() {
        return diferenca;
    }

    public void setDiferenca(String diferenca) {
        this.diferenca = diferenca;
    }

    public String getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(String ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public String getTipoPDV() {
        return tipoPDV;
    }

    public void setTipoPDV(String tipoPDV) {
        this.tipoPDV = tipoPDV;
    }

    public String getHorarioLocal() {
        return horarioLocal;
    }

    public void setHorarioLocal(String horarioLocal) {
        this.horarioLocal = horarioLocal;
    }

}
