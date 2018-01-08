package br.com.lasa.notificacao.audit.config;

import br.com.lasa.notificacao.audit.CustomAuthenticationProvider;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

@EnableMongoAuditing
public class WebSecurityConfiguration
        //extends WebSecurityConfigurerAdapter
{

    //@Autowired
    CustomAuthenticationProvider authenticationProvider;

    //@Override
    public void init(WebSecurity web) {
        web.ignoring().antMatchers("/");
    }

    //@Override
    public void configure(AuthenticationManagerBuilder auth)
            throws Exception {

        //auth.authenticationProvider(authenticationProvider);
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("admin")
                .roles("USER");
    }

    //@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**").authorizeRequests().anyRequest().authenticated();
    }

}
