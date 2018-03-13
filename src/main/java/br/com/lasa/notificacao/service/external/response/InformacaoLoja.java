package br.com.lasa.notificacao.service.external.response;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;


@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY )
public class InformacaoLoja {

    private Integer loja;
    private String centro;

    private String nomeLoja;

    private String nomeCombinado;
    private String regiao;
    private String descricaoRegiao;
    private String distrito;
    private String descricaoDistrito;
    private Collection<HorarioDiario> horarios;
    private String nomeTipo;
    private Integer semHorarioVerao;
    private Integer comHorarioVerao;
    private String tipoPDV;
    private String uf;
    private String cidade;
    private String bairro;
    private String cep;
    private String endereco;
    private String quantidadeLojaCidade;
    private String horarioLocal;

    public Integer getLoja() {
        return loja;
    }

    public void setLoja(Integer loja) {
        this.loja = loja;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getNomeLoja() {
        return nomeLoja;
    }

    @JsonProperty(value = "nome_loja")
    public void setNomeLoja(String nomeLoja) {
        this.nomeLoja = nomeLoja;
    }

    public String getNomeCombinado() {
        return nomeCombinado;
    }

    @JsonProperty(value = "nome_comb")
    public void setNomeCombinado(String nomeCombinado) {
        this.nomeCombinado = nomeCombinado;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getDescricaoRegiao() {
        return descricaoRegiao;
    }

    @JsonProperty(value = "nome_regiao")
    public void setDescricaoRegiao(String descricaoRegiao) {
        this.descricaoRegiao = descricaoRegiao;
    }

    public String getDistrito() {
        return distrito;
    }

    @JsonProperty(value = "id_distrito")
    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDescricaoDistrito() {
        return descricaoDistrito;
    }

    @JsonProperty(value = "desc_distrito")
    public void setDescricaoDistrito(String descricaoDistrito) {
        this.descricaoDistrito = descricaoDistrito;
    }

    public Collection<HorarioDiario> getHorarios() {
        return horarios;
    }

    @JsonProperty(value = "dia_semana")
    public void setHorarios(Collection<HorarioDiario> horarios) {
        this.horarios = horarios;
    }

    public String getNomeTipo() {
        return nomeTipo;
    }

    @JsonProperty(value = "nome_tipo")
    public void setNomeTipo(String nomeTipo) {
        this.nomeTipo = nomeTipo;
    }

    public Integer getSemHorarioVerao() {
        return semHorarioVerao;
    }

    @JsonProperty(value = "sem_horario_verao")
    public void setSemHorarioVerao(Integer semHorarioVerao) {
        this.semHorarioVerao = semHorarioVerao;
    }

    public Integer getComHorarioVerao() {
        return comHorarioVerao;
    }

    @JsonProperty(value = "com_horario_verao")
    public void setComHorarioVerao(Integer comHorarioVerao) {
        this.comHorarioVerao = comHorarioVerao;
    }

    public String getTipoPDV() {
        return tipoPDV;
    }

    @JsonProperty(value = "tipo_pdv")
    public void setTipoPDV(String tipoPDV) {
        this.tipoPDV = tipoPDV;
    }

    public String getUf() {
        return uf;
    }

    @JsonProperty(value = "cod_estado")
    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getHorarioLocal() {
        return horarioLocal;
    }

    public String getQuantidadeLojaCidade() {
        return quantidadeLojaCidade;
    }

    @JsonProperty( value = "quant_loja_cidade")
    public void setQuantidadeLojaCidade(String quantidadeLojaCidade) {
        this.quantidadeLojaCidade = quantidadeLojaCidade;
    }

    @JsonProperty(value = "horario_local")
    public void setHorarioLocal(String horarioLocal) {
        this.horarioLocal = horarioLocal;
    }
}
