package br.com.lasa.notificacao.service;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    /*
    private static final String AD_ROLE_TYPE = "AD";

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final LdapTemplate lasaLDAPTemplate;

    //@Autowired
    public AuthenticationService(
            //@Qualifier(AppConstants.LDAP_TEMPLATE_LASA)
            LdapTemplate lasaLDAPTemplate) {
        this.lasaLDAPTemplate = lasaLDAPTemplate;

    }

    private static String translateLdapException(String msg) {
        return translateLdapException(msg, false);
    }

    private static String translateLdapException(String msg, boolean extended) {
        logger.debug("msg=" + msg);
        String errCode = getLdapMessageCode(msg);
        logger.debug("errCode=" + errCode);
        String ldapMessage = getLdapMessage(errCode, extended);
        logger.debug("ldapMessage=" + errCode);
        if ("".equals(ldapMessage))
            ldapMessage += "Erro de LDAP desconhecido (" + msg + ")";
        return ldapMessage;
    }


    private static String getLdapMessage(String errCode) {
        return getLdapMessage(errCode, false);
    }

    private static String getLdapMessage(String errCode, boolean extended) {
        if ("52e".equals(errCode))
            return "Usuário ou senha inválidos."
                    + (extended ? "[" + errCode + ":senha inválida]" : "");// invalid
        // credential
        if ("525".equals(errCode))
            return "Usuário ou senha inválidos."
                    + (extended ? "[" + errCode + ":usuário não encontrado]"
                    : "");// user not found
        if ("530".equals(errCode))
            return "Acesso não é permitido neste horário.";
        if ("530".equals(errCode))
            return "Acesso não permitido deste computador.";
        if ("532".equals(errCode))
            return "Sua senha está expirada";
        if ("533".equals(errCode))
            return "Sua conta está desabilitada";
        if ("568".equals(errCode))
            return "Seu usuário possui muitos IDs de segurança";
        if ("701".equals(errCode))
            return "Sua conta está expirada";
        if ("773".equals(errCode))
            return "É preciso redefinir sua senha";
        if ("775".equals(errCode))
            return "Sua conta está bloqueada";
        if ("0".equals(errCode))
            return "(DIR_ERROR) Erro na definição do LDAP base. Verifique arquivos de configuração!";
        return "";
    }

    private static String getLdapMessageCode(String msg) {
        Pattern errPatt = Pattern
                .compile(".*LDAP: error code \\d*.*, data (\\w{1,3}).*?");
        Matcher m = errPatt.matcher(msg);
        return (m.matches() ? m.group(1) : "");
    }

    public Map autenticate(String login, String pwd)
            throws AuthenticationException {

        String empresa;

        Filter filter = new AndFilter().and(
                new EqualsFilter("objectClass", "person")).and(
                new EqualsFilter("sAMAccountName", login));


        try {

            boolean autenticacaoFoiBemSucedida = lasaLDAPTemplate.authenticate(
                    "", filter.toString(), pwd);

            empresa = "LAME";

            if (autenticacaoFoiBemSucedida) {

                return searchUser(empresa, login);

            } else {
                logger.debug("Falha de autenticação.");
            }
            return null;
        } catch (UncategorizedLdapException ue) {
            ue.printStackTrace();
            logger.error("Erro ao tentar autenticar. Verifique as configurações de contexto no arquivo applicationContext.xml ou ***-servlet.xml");
            logger.error("erro="
                    + translateLdapException(ue.getMessage(), true));
            throwAuthException(ue);
        } catch (CommunicationException cex) {
            cex.printStackTrace();
            throwAuthException(cex);
        }
        return null;
    }

    public Map login(String login, String password) throws AuthenticationException {
        Map su = autenticate(login, password);
        logger.debug(su.get("principal") + Arrays.toString(Base64.encodeBase64(password.getBytes())));
        return su;
    }

    *//*public void logout() {
        HttpSession httpSession = request.getSession();
        if (httpSession != null)
            httpSession.invalidate();

    }*//*

    *//*@SuppressWarnings("unchecked")
    public List<String> searchAllGroups(String empresa, String dn) {

        List<String> grupos = new ArrayList<>();

        if (empresa.equals("LAME"))
            grupos = lasaLDAPTemplate.search("", "member:1.2.840.113556.1.4.1941:=" + dn, new GroupsMapper());
        else if (empresa.equals("NB2W"))
            grupos = b2wLDAPTemplate.search("", "member:1.2.840.113556.1.4.1941:=" + dn, new GroupsMapper());

        return grupos;
    }*//*

    @SuppressWarnings("unchecked")
    public Map searchUser(String empresa, String login) {
        Filter filter = new AndFilter().and(
                new EqualsFilter("objectClass", "person")).and(
                new EqualsFilter("sAMAccountName", login));

        List<Map> userList = null;
        Map sessao = null;

        if (empresa.equals("LAME"))
            userList = lasaLDAPTemplate.search("",filter.toString(), new SessionUserAttributesMapper());

        if (userList != null && userList.size() > 0) {

            sessao.put("LAIS" , userList.get(0));
        }

        return sessao;
    }

    private void throwAuthException(Exception e) throws AuthenticationException {
        if (e == null)
            return;

        if (e instanceof org.springframework.ldap.AuthenticationException) {
            throw new AuthenticationException(translateLdapException(e.getMessage()));
        } else if (e instanceof  org.springframework.ldap.UncategorizedLdapException) {
            throw new AuthenticationException(translateLdapException(e.getMessage()));
        } else if (e instanceof  org.springframework.ldap.CommunicationException) {
            throw new AuthenticationException(translateComunicationException(e));
        } else {
            throw new AuthenticationException(e.getMessage());
        }
    }

    private String translateComunicationException(Exception e) {
        String def = "Erro ao contactar o servidor de autenticação. Consulte o log do servidor para maiores informações.";
        try {
            CommunicationException ce = (CommunicationException) e;
            Throwable rootThrow = ce.getRootCause();
            if (rootThrow == null)
                return def;

            if (rootThrow instanceof  java.net.UnknownHostException)
                return "Hostname não conhecido: " + rootThrow.getMessage();

            return def;
        } catch (Exception ex) {
            logger.debug("translateCom::ex=" + ex.getMessage() + ">>"
                    + ex.getCause());
            return def;
        }
    }

    @SuppressWarnings("rawtypes")
    private class GroupsMapper implements AttributesMapper {

        @Override
        public Object mapFromAttributes(Attributes attrs)
                throws NamingException {
            String ne = (String) attrs.get("distinguishedName").get();
            return ne;
        }

    }

    @SuppressWarnings("rawtypes")
    private class SessionUserAttributesMapper implements AttributesMapper {
        @Override
        public Object mapFromAttributes(Attributes attrs)
                throws NamingException {
            Map<String, Attributes> value = Collections.singletonMap("value", attrs);
            return value;
        }
    }*/

}
