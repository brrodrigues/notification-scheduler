package br.com.lasa.notificacao.domain.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@Data
@Builder
@Document(collection = "Acesso::Grupo")
public class AccessGroup implements Serializable {

    @Id
    private String id;
    private String name;
    private Collection<Map<String, Collection<Permission>>> permissions;

}
