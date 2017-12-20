package br.com.lasa.notificacao.repository;

import br.com.lasa.notificacao.domain.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

}
