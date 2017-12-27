package br.com.lasa.notificacao.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Data
public class Canal {

    @Id
    private String channelId;
    @Lob
    @ElementCollection(fetch = FetchType.EAGER, targetClass = String.class)
    private Collection<String> users;

}
