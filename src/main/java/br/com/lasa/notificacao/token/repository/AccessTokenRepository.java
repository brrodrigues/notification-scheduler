package br.com.lasa.notificacao.token.repository;

import br.com.lasa.notificacao.token.domain.AccessToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccessTokenRepository extends MongoRepository<AccessToken, String>, AccessTokenCustomRepository {

    AccessToken getFirstByAccessKey(String apiToken);

}
