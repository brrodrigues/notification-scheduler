package br.com.lasa.notificacao.auth.token.service;

import br.com.lasa.notificacao.auth.token.dao.AccessTokenRepository;
import br.com.lasa.notificacao.auth.token.domain.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccessTokenService   {
	
	Logger logger = LoggerFactory.getLogger(AccessTokenService.class);
	
	@Autowired
	AccessTokenRepository accessTokenDao;

	/*@Autowired
	PasswordEncoder passwordEncoder;*/

	public AccessToken getByAccessKey(String apiToken){
		AccessToken accessToken = accessTokenDao.getByAccessKey(apiToken);
		if( accessToken == null)
			return null;
		return accessToken;
	}

	public AccessToken createNewToken(String login, List<String> endpoints){
		String apiToken = login; //passwordEncoder.encode(login);
		String secretKey = generatePrivateKey(); //passwordEncoder.encode(generatePrivateKey());
		AccessToken token = new AccessToken();
		token.setAccessKey(apiToken);
		token.setLogin(login);
		token.setSecretKey(secretKey);
		token.setEndpoints(endpoints);
		return accessTokenDao.insert(token);
	}

	public static String generatePrivateKey(){
		return UUID.randomUUID().toString();
	}

}
