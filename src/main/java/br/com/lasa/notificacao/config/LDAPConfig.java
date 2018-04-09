package br.com.lasa.notificacao.config;

import br.com.lasa.notificacao.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Component;

@Component
public class LDAPConfig {

    @Bean(name = AppConstants.LDAP_CONTEXT_LASA)
    public LdapContextSource contextLASASource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://10.23.93.68:3268");
        contextSource.setBase("dc=lasa,dc=lojasamericanas,dc=com,dc=br");
        contextSource.setUserDn("CN=Usr_app_lasa (Usuario Avançado Portal Consultas AD),OU=Aplicativos,OU=Usuarios,OU=_Sede,OU=LASA.COM,DC=lasa,DC=lojasamericanas,DC=com,DC=br");
        contextSource.setPassword("!portal@2014");
        return contextSource;
    }

    @Bean(name = AppConstants.LDAP_TEMPLATE_LASA)
    public LdapTemplate ldapLASAADTemplate(
            @Autowired
            @Qualifier(value = AppConstants.LDAP_CONTEXT_LASA) LdapContextSource ldapContextSource) {
        return new LdapTemplate(ldapContextSource);
    }

    @Bean(name = AppConstants.LDAP_CONTEXT_B2W)
    public LdapContextSource contextB2WSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://10.21.0.97:3268");
        contextSource.setBase("dc=la,dc=ad,dc=b2w");
        contextSource.setUserDn("CN=LASA_aepero,OU=LASA,OU=Servicos,DC=la,DC=ad,DC=b2w");
        contextSource.setPassword("sT&swusA@4Pu$ruB");
        return contextSource;
    }

    @Bean(name = AppConstants.LDAP_TEMPLATE_B2W)
    public LdapTemplate ldapB2WADTemplate(@Autowired @Qualifier(value = AppConstants.LDAP_CONTEXT_B2W) LdapContextSource ldapContextSource) {
        return new LdapTemplate(ldapContextSource);
    }
}
