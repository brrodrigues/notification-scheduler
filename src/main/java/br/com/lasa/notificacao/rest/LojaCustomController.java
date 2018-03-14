package br.com.lasa.notificacao.rest;

import br.com.lasa.notificacao.constants.HttpHeaderConstants;
import br.com.lasa.notificacao.domain.document.Loja;
import br.com.lasa.notificacao.domain.service.RegiaoDistrito;
import br.com.lasa.notificacao.domain.service.RegiaoDistritoCidade;
import br.com.lasa.notificacao.domain.service.RegiaoDistritoCidadeLoja;
import br.com.lasa.notificacao.rest.resource.*;
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

    /*@CrossOrigin(origins = "*")
    @RequestMapping( value = "lojas/{regiaoId}/regioes/{distritoId}/distritos/{cidadeNome}/cidades/{asdasd}/bruno", method= RequestMethod.GET, produces = "application/hal+json")
    public ResponseEntity<PagedResources<Loja>> get(Pageable pageable) {

        Map<String, Object> maps = new HashMap<>();

        Page<Loja> lojas = lojaService.findAll(pageable);
        PagedResources.wrap(maps.values().iterator());


        return new ResponseEntity(lojas, HttpStatus.OK);
    }*/

    @CrossOrigin(origins = "*")
    @RequestMapping( value = "lojas", method= RequestMethod.PATCH, produces = "application/hal+json")
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
    public ResponseEntity<Resource> findAllByRegiao(@PathVariable("regiaoId") final String codigoRegiao, @RequestParam(value = "tipoLoja", required = false) String tipoLoja) {

        List<RegiaoDistrito> lojaRegiaoDistritoList = lojaService.buscarLojaPorRegiao(codigoRegiao, tipoLoja);

        List<LojaRegiaoResource> parents = new ArrayList<>();

        for (RegiaoDistrito lojaRegiao : lojaRegiaoDistritoList ) {

            Collection<String> distritoList = lojaRegiao.getDistritos();

            if (Objects.isNull(distritoList) || distritoList.isEmpty()) {
                return ResponseEntity.notFound().header(HttpHeaderConstants.REASON, "Nao foi encontrado nenhum distrito com os criterios especificados na consulta").build();
            }

            Link linkSelf = getRegiaoBaseLink(codigoRegiao).withSelfRel();

            Set<ChildrenContent> children = distritoList.stream().map(distritoId -> castRegiaoToChildren(codigoRegiao, distritoId)).collect(Collectors.toSet());

            ParentContent content = ParentContent.builder().
                    selfId(lojaRegiao.getNomeRegiao()).
                    selfName(lojaRegiao.getIdRegiao()).
                    selfType("Regiões").
                    children(children).
                    lojas(lojaRegiao.getLojas()).
                    build();

            LojaRegiaoResource resource = new LojaRegiaoResource(content, linkSelf);

            parents.add(resource);
        }

        return new ResponseEntity(parents, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping (value = "lojas/{regiaoId}/regioes/{distritoId}/distritos", method= RequestMethod.GET, produces = "application/hal+json")
    public ResponseEntity<Resource> findAllByRegiaoDistrito(@PathVariable("regiaoId") String regiaoId, @PathVariable("distritoId") String distritoId,  @RequestParam(value = "tipoLoja", required = false) String tipoLoja) {

        RegiaoDistritoCidade lojaRegiaoDistritoList = lojaService.buscarLojaPorRegiaoEDistrito(regiaoId, distritoId, tipoLoja);

        Collection<String> cidadeList = lojaRegiaoDistritoList.getCidades();

        if (Objects.isNull(cidadeList) || cidadeList.isEmpty()){
            return ResponseEntity.notFound().header(HttpHeaderConstants.REASON, "Nao foi encontrado nenhum cidade com os criterios especificados na consulta").build();
        }

        Link linkSelf       = getDistritoBaseLink(regiaoId, distritoId).withSelfRel();

        Set<ChildrenContent> children = cidadeList.stream().map(cidade -> castDistritoToChildren(regiaoId, distritoId, cidade )).collect(Collectors.toSet());

        ParentContent content = ParentContent.builder().
                selfId(lojaRegiaoDistritoList.getNomeRegiao()).
                selfName(lojaRegiaoDistritoList.getIdRegiao()).
                selfType("Distritos").
                children(children).
                lojas(lojaRegiaoDistritoList.getLojas()).
                build();

        Resource resource = new Resource(content, linkSelf);

        return new ResponseEntity(resource, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping (value = "lojas/{regiaoId}/regioes/{distritoId}/distritos/{cidadeNome}/cidades", method= RequestMethod.GET, produces = "application/hal+json")
    public ResponseEntity<Resource> findAllByRegiaoDistritoECidade(@PathVariable("regiaoId") String regiaoId, @PathVariable("distritoId") String distritoId,  @PathVariable("cidadeNome") String cidadeNome, @RequestParam(value = "tipoLoja", required = false) String tipoLoja) {

        RegiaoDistritoCidadeLoja lojaRegiaoDistritoCidade = lojaService.buscarLojaPorRegiaoEDistritoECidade(regiaoId, distritoId, cidadeNome, tipoLoja);

        List<Map<String, Object>> lojaList = lojaRegiaoDistritoCidade.getLojas();

        if (Objects.isNull(lojaList) || lojaList.isEmpty()){
            return ResponseEntity.notFound().header("reason", "Nao foi encontrado nenhuma loja com os criterios especificados na consulta").build();
        }

        Link linkSelf       = getCidadeBaseLink(regiaoId, distritoId, cidadeNome).withSelfRel();

        ParentContent content = ParentContent.builder().
                selfId(lojaRegiaoDistritoCidade.getNomeRegiao()).
                selfName(lojaRegiaoDistritoCidade.getIdRegiao()).
                selfType("Cidades").
                children(Collections.EMPTY_SET).
                lojas(lojaList).
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

    private ChildrenContent castRegiaoToChildren(String regiao, String distrito) {
        Link distritoLink = getDistritoBaseLink(regiao, distrito).withRel("link");
        Map<String, Object> map = new HashMap<>();
        map.put("id", distrito);
        map.put("link", distritoLink);
        ChildrenContent childrenContent = ChildrenContent.builder().childName("Distritos").childData(Arrays.asList(map)).build();
        return childrenContent;
    }

    private ChildrenContent castDistritoToChildren(String regiao, String distrito, String cidade) {
        Link distritoLink = getCidadeBaseLink(regiao, distrito, cidade).withRel("link");
        Map<String, Object> map = new HashMap<>();
        map.put("id", distrito);
        map.put("link", distritoLink);
        ChildrenContent childrenContent = ChildrenContent.builder().childName("Cidades").childData(Arrays.asList(map)).build();
        return childrenContent;
    }

}
