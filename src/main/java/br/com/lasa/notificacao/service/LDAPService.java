package br.com.lasa.notificacao.service;

//@Component
public class LDAPService {

    /*private final LdapTemplate lasaLdapTemplate;

    //@Autowired
    public LDAPService(
            //@Qualifier(AppConstants.LDAP_TEMPLATE_LASA)
                    LdapTemplate lasaLdapTemplate) {
        this.lasaLdapTemplate = lasaLdapTemplate;
    }

    @SuppressWarnings("unchecked")
    public List findByName(String name) {
        Filter filter = new AndFilter().and(
                new EqualsFilter("objectClass", "person")).and(
                new EqualsFilter("displayName", name + "*"));

        List userList = null;

        userList = lasaLdapTemplate.search("",
                    filter.toString(), new SessionUserAttributesMapper());

        return userList;
    }

    @SuppressWarnings("unchecked")
    public Map findByLogin(String login) {
        Filter filter = new AndFilter().and(
                new EqualsFilter("objectClass", "person")).and(
                new EqualsFilter("sAMAccountName", login));

        List userList;
        Map session = new HashMap();

        userList = lasaLdapTemplate.search("",
                    filter.toString(), new SessionUserAttributesMapper());

        if (userList != null && userList.size() > 0) {
            session.put("user",userList.get(0));
            //session.setEmpresa(empresa);
        }

        return session;
    }

    private class SessionUserAttributesMapper implements AttributesMapper {
        @Override
        public Object mapFromAttributes(Attributes attrs)
                throws NamingException {
            Map map = new HashMap();

            NamingEnumeration<? extends Attribute> all = attrs.getAll();

            while(all.hasMore()){
                Attribute next = all.next();
                map.put(next.getID(), next.get());
            }
            return map;
        }
    }*/
}
