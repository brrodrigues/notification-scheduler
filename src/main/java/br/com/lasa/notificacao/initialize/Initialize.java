package br.com.lasa.notificacao.initialize;

import br.com.lasa.notificacao.token.domain.Endpoint;
import br.com.lasa.notificacao.token.repository.EndpointRepository;
import br.com.lasa.notificacao.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class Initialize {

    @Bean
    CommandLineRunner init(@Autowired final EndpointRepository endpointRepository){

        return (s) -> {
            Endpoint endpointLojaGet = Endpoint.builder().
                    app(AppConstants.APPLICATION_NAME).
                    httpMethod(Arrays.asList("GET", "PUT", "POST")).
                    route("/api/lojas").build();

            Endpoint endpointUsuario = Endpoint.builder().
                    app(AppConstants.APPLICATION_NAME).
                    httpMethod(Arrays.asList("GET")).
                    route("/api/usuarios").build();

            Endpoint endpointConversation = Endpoint.builder().
                    app(AppConstants.APPLICATION_NAME).
                    httpMethod(Arrays.asList("GET", "POST", "PUT")).
                    route("/api/conversations").build();

            Endpoint endpointAccessGroupGet = Endpoint.builder().
                    app(AppConstants.APPLICATION_NAME).
                    httpMethod(Arrays.asList("GET", "POST", "PUT")).
                    route("/api/access-group").build();

            endpointRepository.deleteAll();

            endpointRepository.save(endpointLojaGet);
            endpointRepository.save(endpointUsuario);
            endpointRepository.save(endpointConversation);
            endpointRepository.save(endpointAccessGroupGet);


        };

    }
}
