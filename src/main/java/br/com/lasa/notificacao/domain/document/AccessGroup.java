package br.com.lasa.notificacao.domain.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@Document(collection = "Cadastro_Acesso_Grupo")
public class AccessGroup {

    @Id
    private String id;
    private String name;
    private Map<String, Permission> permissions;

}
