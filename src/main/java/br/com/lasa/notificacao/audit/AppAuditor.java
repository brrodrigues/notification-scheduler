package br.com.lasa.notificacao.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AppAuditor implements AuditorAware<String>{

    @Override
    public String getCurrentAuditor() {

        Optional<Authentication> authentication = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());

        if (!authentication.isPresent())
            return "unknown";

        return authentication.get().getName();


    }
}
