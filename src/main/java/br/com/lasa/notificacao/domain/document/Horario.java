package br.com.lasa.notificacao.domain.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Horario {

    private String dia;
    //@JsonFormat(pattern = "yyyyMMdd'T'hh:mm:ss.SSS", timezone = "UTC")
    @DateTimeFormat(pattern = "yyyyMMdd'T'hh:mm:ss.SSS", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date abertura;
    //@JsonFormat(pattern = "yyyyMMdd'T'hh:mm:ss.SSS", timezone = "UTC")
    @DateTimeFormat(pattern = "yyyyMMdd'T'hh:mm:ss.SSS", iso = DateTimeFormat.ISO.DATE_TIME)
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
