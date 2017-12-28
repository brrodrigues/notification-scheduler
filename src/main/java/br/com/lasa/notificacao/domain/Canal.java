package br.com.lasa.notificacao.domain;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Document
@Data
public class Canal {

    @Id
    private ObjectId id;
    private String channelId;
    private Collection<String> users;

}
