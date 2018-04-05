package br.com.lasa.notificacao.token.service;

import br.com.lasa.notificacao.token.domain.AccessToken;
import br.com.lasa.notificacao.token.domain.Endpoint;
import br.com.lasa.notificacao.token.repository.AccessTokenRepository;
import br.com.lasa.notificacao.token.repository.EndpointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccessTokenService {
	
	Logger logger = LoggerFactory.getLogger(AccessTokenService.class);
	
	@Autowired
	AccessTokenRepository accessTokenDao;

	@Autowired
	EndpointRepository endpointDao;

	public AccessToken getbyToken(String apiToken){
		AccessToken accessToken = accessTokenDao.getFirstByAccessKey(apiToken);
		if(accessToken==null) return null;
		//carregar endpoints
		List<Endpoint> endpoints = endpointDao.findAllById(accessToken.getId());
		logger.trace("ENDPOINTS:"+endpoints);
		accessToken.setEndpoints(endpoints);
		return accessToken;
	}

	public List<AccessToken> getAll(){
		List<AccessToken> tokens = accessTokenDao.findAll();
		for (AccessToken token : tokens) {
			//carregar endpoints
			token.setEndpoints(endpointDao.findAllById(token.getId()));
		}
		return tokens;
	}

	public void deleteToken(String apiToken){
		accessTokenDao.delete(apiToken);
	}
	

	
	public static String generatePrivateKey(){
		return UUID.randomUUID().toString();
	}
	
}
