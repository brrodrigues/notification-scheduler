package br.com.lasa.notificacao.auth.token.domain;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class AccessToken {
	
	private static final long serialVersionUID = -4595669102662461217L;
	
	private String accessKey;
	private String secretKey;
	private String login;
	private List<String> endpoints;

}
