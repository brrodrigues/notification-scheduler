package br.com.lasa.notificacao.domain.lais;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
public class Conversation implements Serializable {

    private boolean isGroup;
    private String id;

}
