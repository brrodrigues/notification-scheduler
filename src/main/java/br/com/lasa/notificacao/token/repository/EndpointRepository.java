package br.com.lasa.notificacao.token.repository;

import br.com.lasa.notificacao.token.domain.Endpoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "/endpoints/")
public interface EndpointRepository extends MongoRepository<Endpoint, String> {

    List<Endpoint> findAllById(Long id);

    @RestResource(exported = false)
    @Override
    void delete(String s);

    @RestResource(exported = false)
    @Override
    void delete(Iterable<? extends Endpoint> entities);

    @RestResource(exported = false)
    @Override
    void deleteAll();

    @RestResource(exported = false)
    @Override
    void delete(Endpoint entity);

    @RestResource(exported = false)
    @Override
    <S extends Endpoint> S save(S entity);

    @RestResource(exported = false)
    @Override
    <S extends Endpoint> List<S> save(Iterable<S> entites);

    @RestResource(exported = false)
    @Override
    <S extends Endpoint> S insert(S entity);

    @RestResource(exported = false)
    @Override
    <S extends Endpoint> List<S> insert(Iterable<S> entities);
}
