package br.com.lasa.notificacao.rest;

import br.com.lasa.notificacao.domain.document.Loja;
import br.com.lasa.notificacao.rest.resource.DistrictContent;
import br.com.lasa.notificacao.rest.resource.RegionContent;
import br.com.lasa.notificacao.rest.resource.StoreContent;
import br.com.lasa.notificacao.rest.resource.TipoLojaResource;
import br.com.lasa.notificacao.service.LojaService;
import br.com.lasa.notificacao.service.external.response.InformacaoLoja;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@BasePathAwareController
public class LojaCustomController {

    @Value("${spring.data.rest.base-path}")
    private String basePath;

    private final String REST_PATH = "/lojas";

    @Autowired
    private LojaService lojaService;

    /*@CrossOrigin(origins = "*")
    @RequestMapping( value = "lojas/{regiaoId}/regioes/{distritoId}/distritos/{cidadeNome}/cidades/{asdasd}/bruno", httpMethod= RequestMethod.GET, produces = "application/hal+json")
    public ResponseEntity<PagedResources<Loja>> get(Pageable pageable) {

        Map<String, Object> maps = new HashMap<>();

        Page<Loja> lojas = lojaService.findAll(pageable);
        PagedResources.wrap(maps.values().iterator());


        return new ResponseEntity(lojas, HttpStatus.OK);
    }*/

    @CrossOrigin(value = "*")
    @RequestMapping( value = "lojas", method= RequestMethod.PATCH, produces = "application/hal+json")
    public ResponseEntity<Loja> patch(@RequestBody Loja loja) {

        Loja atualizar = lojaService.atualizar(loja);

        return new ResponseEntity(atualizar, HttpStatus.OK);
    }

    @CrossOrigin(value = "*")
    @RequestMapping( value = "lojas/search/findAllTipoLojas", method= RequestMethod.GET, produces = "application/hal+json")
    public ResponseEntity<Resources> findAllTipoLoja (){

        List<String> tipoLojas = lojaService.listarTipoLojas();

        TipoLojaResource resource = new TipoLojaResource(tipoLojas);

        Resources resources = new Resources(Arrays.asList(resource));

        return ResponseEntity.ok(resources);
    };

    @CrossOrigin(value = "*")
    @RequestMapping (value = "lojas/{id}", method= RequestMethod.PATCH, produces = "application/hal+json")
    public ResponseEntity<Loja> patch(@PathVariable("id") String id, @RequestBody Loja loja) {

        loja.setId(id);
        Loja atualizar = lojaService.atualizar(loja);

        return new ResponseEntity(atualizar, HttpStatus.OK);
    }

    @CrossOrigin(value = "*")
    @RequestMapping (value = "lojas/{regiaoId}/regioes", method= RequestMethod.GET, produces = "application/hal+json")
    public ResponseEntity<Resource> findAllByRegiao(@PathVariable("regiaoId") final String codigoRegiao) {

        List<Loja> lojaRegiaoDistritoList = lojaService.findAll();

        List<RegionContent> regionContents = new ArrayList<>();

        if (lojaRegiaoDistritoList != null && !lojaRegiaoDistritoList.isEmpty()) {
            for (Loja loja : lojaRegiaoDistritoList) {
                Map<String, Object> metadata = loja.getMetadata();
                //Valida previamente se existe o atributo adicional no mapa
                if (metadata.containsKey("adicional")){
                    InformacaoLoja informacaoLoja = (InformacaoLoja) metadata.get("adicional");

                    String regiao = informacaoLoja.getRegiao();
                    String descricaoRegiao = informacaoLoja.getDescricaoRegiao();
                    String distrito = informacaoLoja.getDistrito();
                    String descricaoDistrito = informacaoLoja.getDescricaoDistrito();
                    String centro =  informacaoLoja.getCentro();
                    String nomeCombinado =  informacaoLoja.getNomeCombinado();
                    String tipoLoja = informacaoLoja.getNomeTipo();

                    RegionContent regionContent = RegionContent.builder().type("REGIAO").label(descricaoRegiao).children(new ArrayList<>()).value(regiao).build();
                    DistrictContent districtContent = DistrictContent.builder().type("DISTRITO").label(descricaoDistrito).children(new HashSet<>()).value(distrito).build();
                    StoreContent storeContent = StoreContent.builder().storeType(tipoLoja).label(nomeCombinado).value(centro).build();

                    int regionIndex = regionContents.indexOf(regionContent) ;

                    if (regionIndex < 0) {
                        regionContents.add(regionContent);
                        regionIndex = 0;
                    }
                    regionContent = regionContents.get(regionIndex);

                    List<DistrictContent> districtContents = regionContent.getChildren();

                    int districtIndex = districtContents.indexOf(districtContent);

                    if (districtIndex < 0 ) {
                        districtContents.add(districtContent);
                        districtIndex = 0;
                    }
                    districtContent = districtContents.get(districtIndex);

                    Set<StoreContent> storeSet = districtContent.getChildren();

                    storeSet.add(storeContent);

                    regionContent.getChildren().set(districtIndex, districtContent);
                    regionContents.set(regionIndex, regionContent);


                    /*if (root.contains(regionContent)){
                        int i = root.indexOf(regionContent);
                    }

                    RegionContent parentContent = root.get(i);

                    if (parentContent.getChildren().contains(districtContent));*/

                    /*Map<RegionContent, Set<StoreContent>> districtMap = rootMap.getOrDefault(regionContent, new HashMap<>());//Se nao existir a regiao, este sera criado
                    Set<StoreContent> storeMap = districtMap.getOrDefault(regionContent, new HashSet<>());//Se nao existir a regiao, este sera criado

                    storeMap.add(storeContent);
                    districtMap.put(districtContent, storeMap);
                    rootMap.put(regionContent,districtMap);*/


                }
            }

        }

        /*for (Loja lojaRegiao : lojaRegiaoDistritoList ) {

            RegionContent content = RegionContent.builder().
                    selfId(lojaRegiao.getIdRegiao()).
                    selfType("Regiões").
                    selfName(lojaRegiao.getIdRegiao().concat("-").concat(lojaRegiao.getNomeRegiao())).
                    children(distritoMapList).
                    lojas(lojaRegiao.getLojas()).
                    build();

            Resource resource = new Resource(content, linkSelf);

            parents.add(resource);
        }*/

        return new ResponseEntity(regionContents, HttpStatus.OK);
    }

    private Map<String, Object> mapMetadata(Loja loja) {
        return loja.getMetadata();
    }

    /*@CrossOrigin(value = "*")
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

            Set<StoreContent> children = cidadeList.stream().mapMetadata(cidade -> castCidadeToChildren(distritoCidade.getIdRegiao(), distritoCidade.getIdDistrito(), cidade )).collect(Collectors.toSet());

            RegionContent content = RegionContent.builder().
                    selfId(distritoCidade.getIdDistrito()).
                    selfName(distritoCidade.getIdDistrito().concat("-").concat(distritoCidade.getNomeDistrito())).
                    selfType("Distritos").
                    children(children).
                    lojas(distritoCidade.getLojas()).
                    build();

            Resource resource = new Resource(content, linkSelf);

            parents.add(resource);
        }

        return new ResponseEntity(parents, HttpStatus.OK);
    }

    @CrossOrigin(value = "*")
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

            RegionContent content = RegionContent.builder().
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
    }*/

    /*private ControllerLinkBuilder getBaseLink(){
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
    }*/

   /* private StoreContent castCidadeToChildren(String regiao, String distrito, RegiaoDistritoCidade.Cidade cidade) {
        Link distritoLink = getCidadeBaseLink(regiao, distrito, cidade.getId()).withRel("link");
        Map<String, Object> mapMetadata = new HashMap<>();
        StoreContent childrenContent = StoreContent.builder().link(distritoLink).name(cidade.getId()).id(cidade.getId()).build();

        return childrenContent;
    }*/

}
