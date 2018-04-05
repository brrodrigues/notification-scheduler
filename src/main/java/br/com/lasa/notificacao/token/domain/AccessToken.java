package br.com.lasa.notificacao.token.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@ToString
@Document(collection = "Cadastro_Acesso_Token")
public class AccessToken implements Serializable {
	
	private static final long serialVersionUID = -4595669102662461217L;

	@Id
	private Long id;
	private String url;
	private String accessKey;
	private List<Endpoint> endpoints;

}
