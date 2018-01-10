package br.com.lasa.notificacao.rest.response;

import lombok.Builder;

import java.util.Date;

@Builder
public class DataUltimaVenda {

    private Date dataConsulta;
    private Date dataUltimaVenda;

    public Date getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(Date dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public Date getDataUltimaVenda() {
        return dataUltimaVenda;
    }

    public void setDataUltimaVenda(Date dataUltimaVenda) {
        this.dataUltimaVenda = dataUltimaVenda;
    }
}
