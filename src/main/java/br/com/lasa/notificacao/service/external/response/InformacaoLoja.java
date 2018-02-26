package br.com.lasa.notificacao.service.external.response;


import com.fasterxml.jackson.annotation.JsonProperty;

public class InformacaoLoja {

    private Integer loja;
    private String centro;
    @JsonProperty(value = "nome_loja")
    private String nomeLoja;
    @JsonProperty(value = "nome_comb")
    private String nomeCombinado;
    private String regiao;
    @JsonProperty(value = "id_distrito")
    private String distrito;
    @JsonProperty(value = "desc_distrito")
    private String descricaoDistrito;
    @JsonProperty(value = "hora_abertura")
    private String horaAbertura;
    @JsonProperty(value = "hora_fechamento")
    private String horaFechamento;
    @JsonProperty(value = "dia_semana")
    private Integer diaSemana;
    @JsonProperty(value = "dia_ext_semana")
    private String diaExtensoSemana;
    private Integer situacao;
    private String status;
    @JsonProperty(value = "nome_tipo")
    private String nomeTipo;
    @JsonProperty(value = "sem_horario_verao")
    private Integer semHorarioVerao;
    @JsonProperty(value = "com_horario_verao")
    private Integer comHorarioVerao;
    @JsonProperty(value = "tipo_pdv")
    private String tipoPDV;
    @JsonProperty(value = "cod_estado")
    private String uf;
    private String bairro;
    private String cep;
    private String endereco;
    @JsonProperty(value = "horario_local")
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

    public void setNomeLoja(String nomeLoja) {
        this.nomeLoja = nomeLoja;
    }

    public String getNomeCombinado() {
        return nomeCombinado;
    }

    public void setNomeCombinado(String nomeCombinado) {
        this.nomeCombinado = nomeCombinado;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDescricaoDistrito() {
        return descricaoDistrito;
    }

    public void setDescricaoDistrito(String descricaoDistrito) {
        this.descricaoDistrito = descricaoDistrito;
    }

    public String getHoraAbertura() {
        return horaAbertura;
    }

    public void setHoraAbertura(String horaAbertura) {
        this.horaAbertura = horaAbertura;
    }

    public String getHoraFechamento() {
        return horaFechamento;
    }

    public void setHoraFechamento(String horaFechamento) {
        this.horaFechamento = horaFechamento;
    }

    public Integer getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getDiaExtensoSemana() {
        return diaExtensoSemana;
    }

    public void setDiaExtensoSemana(String diaExtensoSemana) {
        this.diaExtensoSemana = diaExtensoSemana;
    }

    public Integer getSituacao() {
        return situacao;
    }

    public void setSituacao(Integer situacao) {
        this.situacao = situacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNomeTipo() {
        return nomeTipo;
    }

    public void setNomeTipo(String nomeTipo) {
        this.nomeTipo = nomeTipo;
    }

    public Integer getSemHorarioVerao() {
        return semHorarioVerao;
    }

    public void setSemHorarioVerao(Integer semHorarioVerao) {
        this.semHorarioVerao = semHorarioVerao;
    }

    public Integer getComHorarioVerao() {
        return comHorarioVerao;
    }

    public void setComHorarioVerao(Integer comHorarioVerao) {
        this.comHorarioVerao = comHorarioVerao;
    }

    public String getTipoPDV() {
        return tipoPDV;
    }

    public void setTipoPDV(String tipoPDV) {
        this.tipoPDV = tipoPDV;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
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

    public void setHorarioLocal(String horarioLocal) {
        this.horarioLocal = horarioLocal;
    }
}
