package br.com.lasa.notificacao.rest;

import br.com.lasa.notificacao.constants.HttpHeaderConstants;
import br.com.lasa.notificacao.domain.document.Loja;
import br.com.lasa.notificacao.domain.service.RegiaoDistrito;
import br.com.lasa.notificacao.domain.service.RegiaoDistritoCidade;
import br.com.lasa.notificacao.domain.service.RegiaoDistritoCidadeLoja;
import br.com.lasa.notificacao.rest.resource.LojaRegiaoContent;
import br.com.lasa.notificacao.rest.resource.LojaRegiaoDistritoCidadeContent;
import br.com.lasa.notificacao.rest.resource.LojaRegiaoDistritoContent;
import br.com.lasa.notificacao.rest.resource.LojaRegiaoResource;
import br.com.lasa.notificacao.service.LojaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@BasePathAwareController
public class LojaCustomController {

    @Value("${spring.data.rest.base-path}")
    private String basePath;

    private final String REST_PATH = "/lojas";

    @Autowired
    private LojaService lojaService;

    @CrossOrigin(origins = "*")
    @RequestMapping(method= RequestMethod.PATCH, produces = "application/hal+json")
    public ResponseEntity<Loja> patch(@RequestBody Loja loja) {

        Loja atualizar = lojaService.atualizar(loja);

        return new ResponseEntity(atualizar, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping (value = "lojas/{id}", method= RequestMethod.PATCH, produces = "application/hal+json")
    public ResponseEntity<Loja> patch(@PathVariable("id") String id, @RequestBody Loja loja) {

        loja.setId(id);
        Loja atualizar = lojaService.atualizar(loja);

        return new ResponseEntity(atualizar, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping (value = "lojas/{regiaoId}/regioes", method= RequestMethod.GET, produces = "application/hal+json")
    public ResponseEntity<Resource> findAllByRegiao(@PathVariable("regiaoId") String codigoRegiao, @RequestParam(value = "tipoLoja", required = false) String tipoLoja) {

        RegiaoDistrito lojaRegiao = lojaService.buscarLojaPorRegiao(codigoRegiao, tipoLoja);

        Collection<String> distritoList = lojaRegiao.getDistritos();

        if ( Objects.isNull(distritoList) || distritoList.isEmpty()) {
            return ResponseEntity.notFound().header(HttpHeaderConstants.REASON, "Nao foi encontrado nenhum distrito com os criterios especificados na consulta").build();
        }

        Link linkSelf = getRegiaoBaseLink(codigoRegiao).withSelfRel();

        List<Map<String, Object>> distritoLinks = distritoList.stream().map(s -> {
            Link distritoLink = getDistritoBaseLink(codigoRegiao, s).withRel("link");
            Map<String, Object> distritos = new HashMap<>();
            distritos.put("id", s );
            distritos.put("link", distritoLink);
            return distritos;
        }).collect(Collectors.toList());

        LojaRegiaoContent valor = LojaRegiaoContent.builder().
                lojas(lojaRegiao.getLojas()).
                nomeRegiao(lojaRegiao.getNomeRegiao()).
                idRegiao(lojaRegiao.getIdRegiao()).
                distritos(distritoLinks).
                build();

        LojaRegiaoResource lojaRegiaoResource = new LojaRegiaoResource(valor, linkSelf);

        return new ResponseEntity(lojaRegiaoResource, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping (value = "lojas/{regiaoId}/regioes/{distritoId}/distritos", method= RequestMethod.GET, produces = "application/hal+json")
    public ResponseEntity<Resource> findAllByRegiaoDistrito(@PathVariable("regiaoId") String regiaoId, @PathVariable("distritoId") String distritoId,  @RequestParam(value = "tipoLoja", required = false) String tipoLoja) {

        RegiaoDistritoCidade lojaRegiaoDistrito = lojaService.buscarLojaPorRegiaoEDistrito(regiaoId, distritoId, tipoLoja);

        Collection<String> cidadeList = lojaRegiaoDistrito.getCidades();

        if (Objects.isNull(cidadeList) || cidadeList.isEmpty()){
            return ResponseEntity.notFound().header(HttpHeaderConstants.REASON, "Nao foi encontrado nenhum cidade com os criterios especificados na consulta").build();
        }

        Link linkSelf       = getDistritoBaseLink(regiaoId, distritoId).withSelfRel();

        List<Map<String, Object>> cidadeLinks = cidadeList.stream().map(s -> {
            Link distritoLink = getCidadeBaseLink(regiaoId, distritoId, s).withRel("link");
            Map<String, Object> distritos = new HashMap<>();
            distritos.put("id", s );
            distritos.put("link", distritoLink);
            return distritos;
        }).collect(Collectors.toList());

        LojaRegiaoDistritoContent content = LojaRegiaoDistritoContent.builder().
                cidades(cidadeLinks).
                idDistrito(lojaRegiaoDistrito.getIdDistrito()).
                lojas(lojaRegiaoDistrito.getLojas()).
                nomeDistrito(lojaRegiaoDistrito.getNomeDistrito()).
                nomeRegiao(lojaRegiaoDistrito.getNomeRegiao()).
                idRegiao(lojaRegiaoDistrito.getIdRegiao()).build();

        Resource resource = new Resource(content, linkSelf);

        return new ResponseEntity(resource, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping (value = "lojas/{regiaoId}/regioes/{distritoId}/distritos/{cidadeNome}/cidades", method= RequestMethod.GET, produces = "application/hal+json")
    public ResponseEntity<Resource> findAllByRegiaoDistritoECidade(@PathVariable("regiaoId") String regiaoId, @PathVariable("distritoId") String distritoId,  @PathVariable("cidadeNome") String cidadeNome, @RequestParam(value = "tipoLoja", required = false) String tipoLoja) {

        RegiaoDistritoCidadeLoja lojaRegiaoDistrito = lojaService.buscarLojaPorRegiaoEDistritoECidade(regiaoId, distritoId, cidadeNome, tipoLoja);

        List<Map<String, Object>> lojaList = lojaRegiaoDistrito.getLojas();

        if (Objects.isNull(lojaList) || lojaList.isEmpty()){
            return ResponseEntity.notFound().header("reason", "Nao foi encontrado nenhuma loja com os criterios especificados na consulta").build();
        }

        Link linkSelf       = getCidadeBaseLink(regiaoId, distritoId, cidadeNome).withSelfRel();

        LojaRegiaoDistritoCidadeContent content = LojaRegiaoDistritoCidadeContent.builder().
                idDistrito(lojaRegiaoDistrito.getIdDistrito()).
                nomeDistrito(lojaRegiaoDistrito.getNomeDistrito()).
                idRegiao(lojaRegiaoDistrito.getIdRegiao()).
                nomeCidade(lojaRegiaoDistrito.getCidade()).
                lojas(lojaList).
                nomeRegiao(lojaRegiaoDistrito.getNomeRegiao()).
                build();

        Resource resource = new Resource(content, linkSelf);

        return new ResponseEntity(resource, HttpStatus.OK);
    }

    private ControllerLinkBuilder getBaseLink(){
        return ControllerLinkBuilder.linkTo(LojaCustomController.class).slash(basePath).slash(REST_PATH);
    }

    private ControllerLinkBuilder getRegiaoBaseLink(String regiao) {
        return getBaseLink().slash(regiao).slash("regioes");
    }

    private ControllerLinkBuilder getDistritoBaseLink(String regiao, String distrito){
        return getRegiaoBaseLink(regiao).slash(distrito).slash("distritos");
    }

    private ControllerLinkBuilder getCidadeBaseLink(String regiao, String distrito, String cidade){
        return getRegiaoBaseLink(regiao).slash(distrito).slash("distritos").slash(cidade).slash("cidades");
    }

}
