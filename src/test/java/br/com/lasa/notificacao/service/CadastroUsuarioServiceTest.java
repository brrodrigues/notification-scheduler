package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.UsuarioNotificacao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@SpringBootTest
@RunWith(SpringRunner.class)
public class CadastroUsuarioServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @Qualifier("usuarioJonatasLais")
    private UsuarioNotificacao usuarioJonatas;

    @Autowired
    @Qualifier("usuarioGustavoLais")
    private UsuarioNotificacao usuarioGustavo;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void givenRequisicaoWhenNullThenStatusBadRequestError() throws Exception {
        mockMvc.perform(post("/api/cadastroGGL", null)).andExpect(status().isBadRequest());
    }

}