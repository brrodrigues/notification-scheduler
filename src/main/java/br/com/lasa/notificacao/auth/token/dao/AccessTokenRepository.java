package br.com.lasa.notificacao.auth.token.dao;

import br.com.lasa.notificacao.auth.token.domain.AccessToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccessTokenRepository extends MongoRepository<AccessToken, String > {

	AccessToken getByAccessKey(String accessKey);

}
