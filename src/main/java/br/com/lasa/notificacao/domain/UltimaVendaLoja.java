package br.com.lasa.notificacao.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UltimaVendaLoja {

    private LocalDateTime dataConsulta;
    private LocalDateTime dataUltimaConsulta;

}
