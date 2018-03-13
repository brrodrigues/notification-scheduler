package br.com.lasa.notificacao.aspect.repository;

import br.com.lasa.notificacao.aspect.ApplicationErrorTrace;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface ApplicationTraceRepository extends MongoRepository<ApplicationErrorTrace, String> {

}
