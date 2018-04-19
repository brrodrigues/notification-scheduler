package br.com.lasa.notificacao.config;

//@Component
public class LDAPConfig {

    /*@Bean(name = AppConstants.LDAP_CONTEXT_LASA)
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
    }*/

}
