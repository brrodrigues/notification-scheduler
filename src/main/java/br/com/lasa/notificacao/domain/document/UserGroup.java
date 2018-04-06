package br.com.lasa.notificacao.domain.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "Cadastro_Usuario_Grupo")
public class UserGroup {

    @Id
    private String login;
    private List<AccessGroup> accessGroups;

}
