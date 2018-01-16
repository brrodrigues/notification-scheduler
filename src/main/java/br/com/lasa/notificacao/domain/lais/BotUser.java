package br.com.lasa.notificacao.domain.lais;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BotUser implements Serializable {

    private String id;
    private String name;

}
