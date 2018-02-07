package br.com.lasa.notificacao.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Horario {

    private String dia;
    @JsonFormat(pattern = "yyyyMMdd'T'hh:mm:ss.SSSZ", timezone = "CET")
    private Date abertura;
    @JsonFormat(pattern = "yyyyMMdd'T'hh:mm:ss.SSS", timezone = "CET")
    private Date fechamento;


    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public Date getAbertura() {
        return abertura;
    }

    public void setAbertura(Date abertura) {
        this.abertura = abertura;
    }

    public Date getFechamento() {
        return fechamento;
    }

    public void setFechamento(Date fechamento) {
        this.fechamento = fechamento;
    }
}
