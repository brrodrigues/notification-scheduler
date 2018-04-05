package br.com.lasa.notificacao.token.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;

@Data
@ToString
@Builder
@Document(collection = "Lista_Endpoint")
public class Endpoint {

	private static final long serialVersionUID = -4905635112760806931L;

	@JsonIgnore
	@Id
	private String id;
	private String app;
	private String route;
	private Collection<String> httpMethod;

}
