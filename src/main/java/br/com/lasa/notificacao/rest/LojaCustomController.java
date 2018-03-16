package br.com.lasa.notificacao.rest;

import br.com.lasa.notificacao.constants.HttpHeaderConstants;
import br.com.lasa.notificacao.domain.document.Loja;
import br.com.lasa.notificacao.domain.service.RegiaoDistrito;
import br.com.lasa.notificacao.domain.service.RegiaoDistritoCidade;
import br.com.lasa.notificacao.domain.service.RegiaoDistritoCidadeLoja;
import br.com.lasa.notificacao.rest.resource.ChildrenContent;
import br.com.lasa.notificacao.rest.resource.ParentContent;
import br.com.lasa.notificacao.rest.resource.TipoLojaResource;
import br.com.lasa.notificacao.service.LojaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
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
    @RequestMapping( value = "lojas/search/findAllTipoLojas", method= RequestMethod.GET, produces = "application/hal+json")
    public ResponseEntity<Resources> findAllTipoLoja (){

        List<String> tipoLojas = lojaService.listarTipoLojas();

        TipoLojaResource resource = new TipoLojaResource(tipoLojas);

        Resources resources = new Resources(Arrays.asList(resource));

        return ResponseEntity.ok(resources);
    };

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

        List<Resource> parents = new ArrayList<>();

        for (RegiaoDistrito lojaRegiao : lojaRegiaoDistritoList ) {

            List<RegiaoDistrito.Distrito> distritoList = lojaRegiao.getDistritos();

            if (Objects.isNull(distritoList) || distritoList.isEmpty()) {
                return ResponseEntity.notFound().header(HttpHeaderConstants.REASON, "Nao foi encontrado nenhum distrito com os criterios especificados na consulta").build();
            }

            Link linkSelf = getRegiaoBaseLink(codigoRegiao).withSelfRel();

            Set<ChildrenContent> children = distritoList.stream().map(distrito -> castRegiaoToChildren(codigoRegiao, distrito)).collect(Collectors.toSet());

            ParentContent content = ParentContent.builder().
                    selfId(lojaRegiao.getIdRegiao()).
                    selfType("Regiões").
                    selfName(lojaRegiao.getNomeRegiao()).
                    children(children).
                    lojas(lojaRegiao.getLojas()).
                    build();

            Resource resource = new Resource(content, linkSelf);

            parents.add(resource);
        }

        return new ResponseEntity(parents, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping (value = "lojas/{regiaoId}/regioes/{distritoId}/distritos", method= RequestMethod.GET, produces = "application/hal+json")
    public ResponseEntity<Resource> findAllByRegiaoDistrito(@PathVariable("regiaoId") String regiaoId, @PathVariable("distritoId") String distritoId,  @RequestParam(value = "tipoLoja", required = false) String tipoLoja) {

        List<RegiaoDistritoCidade> lojaRegiaoDistritoList = lojaService.buscarLojaPorRegiaoEDistrito(regiaoId, distritoId, tipoLoja);

        List<Resource> parents = new ArrayList<>();

        for (RegiaoDistritoCidade distritoCidade :
                lojaRegiaoDistritoList) {

            Collection<RegiaoDistritoCidade.Cidade> cidadeList = distritoCidade.getCidades();

            if (Objects.isNull(cidadeList) || cidadeList.isEmpty()){
                return ResponseEntity.notFound().header(HttpHeaderConstants.REASON, "Nao foi encontrado nenhum cidade com os criterios especificados na consulta").build();
            }

            Link linkSelf       = getDistritoBaseLink(distritoCidade.getIdRegiao(), distritoCidade.getIdDistrito()).withSelfRel();

            Set<ChildrenContent> children = cidadeList.stream().map(cidade -> castCidadeToChildren(distritoCidade.getIdRegiao(), distritoCidade.getIdDistrito(), cidade )).collect(Collectors.toSet());

            ParentContent content = ParentContent.builder().
                    selfId(distritoCidade.getIdDistrito()).
                    selfName(distritoCidade.getNomeDistrito()).
                    selfType("Distritos").
                    children(children).
                    lojas(distritoCidade.getLojas()).
                    build();

            Resource resource = new Resource(content, linkSelf);

            parents.add(resource);
        }

        return new ResponseEntity(parents, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping (value = "lojas/{regiaoId}/regioes/{distritoId}/distritos/{cidadeNome}/cidades", method= RequestMethod.GET, produces = "application/hal+json")
    public ResponseEntity<Resource> findAllByRegiaoDistritoECidade(@PathVariable("regiaoId") String regiaoId, @PathVariable("distritoId") String distritoId,  @PathVariable("cidadeNome") String cidadeNome, @RequestParam(value = "tipoLoja", required = false) String tipoLoja) {

        List<RegiaoDistritoCidadeLoja> lojaRegiaoDistritoCidadeList = lojaService.buscarLojaPorRegiaoEDistritoECidade(regiaoId, distritoId, cidadeNome, tipoLoja);

        List<Resource> parents = new ArrayList<>();

        for (RegiaoDistritoCidadeLoja distritoCidadeLoja:
                lojaRegiaoDistritoCidadeList) {

            List<Map<String, Object>> lojaList = distritoCidadeLoja.getLojas();

            if (Objects.isNull(lojaList) || lojaList.isEmpty()){
                return ResponseEntity.notFound().header("reason", "Nao foi encontrado nenhuma loja com os criterios especificados na consulta").build();
            }

            Link linkSelf = getCidadeBaseLink(regiaoId, distritoId, cidadeNome).withSelfRel();

            ParentContent content = ParentContent.builder().
                selfName(distritoCidadeLoja.getCidade()).
                selfId(distritoCidadeLoja.getCidade()).
                selfType("Cidades").
                children(Collections.EMPTY_SET).
                lojas(lojaList).
                build();

            Resource resource = new Resource(content, linkSelf);

            parents.add(resource);
        }

        return new ResponseEntity(parents, HttpStatus.OK);
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

    private ChildrenContent castRegiaoToChildren(String regiao, RegiaoDistrito.Distrito distrito) {
        Link distritoLink = getDistritoBaseLink(regiao, distrito.getId()).withRel("link");
        Map<String, Object> map = new HashMap<>();
        map.put("id", distrito.getId());
        map.put("name", distrito.getName());
        map.put("link", distritoLink);
        ChildrenContent childrenContent = ChildrenContent.builder().childName("Distritos").childData(Arrays.asList(map)).build();
        return childrenContent;
    }

    private ChildrenContent castCidadeToChildren(String regiao, String distrito, RegiaoDistritoCidade.Cidade cidade) {
        Link distritoLink = getCidadeBaseLink(regiao, distrito, cidade.getId()).withRel("link");
        Map<String, Object> map = new HashMap<>();
        map.put("id", cidade.getId());
        map.put("name", cidade.getId());
        map.put("link", distritoLink);
        ChildrenContent childrenContent = ChildrenContent.builder().childName("Cidades").childData(Arrays.asList(map)).build();
        return childrenContent;
    }

}
