package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.document.Loja;
import br.com.lasa.notificacao.domain.service.RegiaoDistrito;
import br.com.lasa.notificacao.domain.service.RegiaoDistritoCidade;
import br.com.lasa.notificacao.domain.service.RegiaoDistritoCidadeLoja;
import br.com.lasa.notificacao.repository.LojaRepository;
import br.com.lasa.notificacao.service.external.CalendarioDeLojaExternalService;
import br.com.lasa.notificacao.service.external.response.CalendarioDeLoja;
import br.com.lasa.notificacao.service.external.response.InformacaoLoja;
import br.com.lasa.notificacao.util.DateTimeFormatterUtils;
import br.com.lasa.notificacao.util.JsonMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class LojaServiceImpl implements LojaService {

    @Autowired
    private LojaRepository lojaRepository;

    @Autowired
    private CalendarioDeLojaExternalService calendarioDeLojaExternalService;

    @Autowired
    private DateTimeFormatterUtils dateTimeFormatterUtils;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ZoneId zoneId;

    private static Map<String, Object> mapearInformacaoLoja(Loja loja) {
        Map<String, Object> maps = new HashMap<>();
            maps.put("numero", loja.getId());
        maps.put("nome", loja.getNomeLoja());
        Object tipoLoja = JsonMap.searchFromXPath(loja.getMetadata(), "$.adicional.nome_tipo");
        maps.put("tipoLoja", tipoLoja);
        return maps;
    }

    private RegiaoDistritoCidade.Cidade mapearNomeCidade(InformacaoLoja informacaoLoja) {
        RegiaoDistritoCidade.Cidade cidade = new RegiaoDistritoCidade.Cidade();
        cidade.setId(informacaoLoja.getCidade());
        return cidade;

    }

    @Override
    public Loja buscarLojaPorCodigo(String codigoLoja) {
        return lojaRepository.findOne(codigoLoja);
    }

    public Loja atualizar(String id, Loja lojaParam) {
        //Atribui o horario de abertura e fechamento para todos os dias ao atualizar
        Loja lojaId = lojaRepository.findOne(id);

        if (lojaId == null) {
            throw new IllegalArgumentException("Nao foi possivel localizar os dados da loja para serem atualizados. Verifique as informacoes enviadas para o servidor.");
        }

        CalendarioDeLoja calendarioDeLoja = calendarioDeLojaExternalService.buscarCalendarioDaSemanaDaLoja(id, lojaParam.getResponsavelGeral());

        Loja loja = calendarioDeLojaExternalService.montarEstrutura(calendarioDeLoja);

        lojaId = loja;
        lojaId.setId(id);
        lojaId.setHorarios(calendarioDeLoja.getHorarios());
        lojaId.setMetadata(calendarioDeLoja.getMetadata());

        Loja save = lojaRepository.save(lojaId);
        return save;
    }

   public Loja atualizar(Loja lojaParam) {
        //Atribui o horario de abertura e fechamento para todos os dias ao atualizar
        Assert.isNull(lojaParam, "Nao foi possivel localizar os dados da loja para serem atualizados. Verifique as informacoes enviadas para o servidor.");

        CalendarioDeLoja calendarDeLoja = calendarioDeLojaExternalService.buscarCalendarioDaSemanaDaLoja(lojaParam.getId(), lojaParam.getResponsavelGeral());

       Loja loja = calendarioDeLojaExternalService.montarEstrutura(calendarDeLoja);

       lojaRepository.delete(lojaParam.getId());
        Loja save = lojaRepository.save(lojaParam);
        return save;
   }

    /**
     *
     * @param regiaoId
     * @param tipoLoja
     * @return
    */
    @Override
    public List<RegiaoDistrito> buscarLojaPorRegiao(String regiaoId, String tipoLoja) {

        Assert.notNull(regiaoId, "Informe o numero");

        Map<String, Object> metadata = getMetadataByParam(tipoLoja, regiaoId);

        Loja lojaExample = Loja.builder().metadata(metadata).build();

        Example<Loja> example = Example.of(lojaExample);

        final List<Loja> allByRegiao = lojaRepository.findAll(example);

        List<RegiaoDistrito> regiaoDistritos = new ArrayList<>();

        if (allByRegiao != null && !allByRegiao.isEmpty()) {

            //Agrupa as lojas por regiao
            Map<String, List<Loja>> regiaoMap = allByRegiao.
                    stream().
                    collect(Collectors.groupingBy(o -> String.valueOf(((InformacaoLoja) o.getMetadata().get("adicional")).getRegiao())));

            regiaoMap.forEach( (regiao, lojas) -> {
                InformacaoLoja informacaoLoja = extractInformacaoLoja(lojas.get(0));
                List<Map<String, Object>> lojaMap       = getLojas(lojas);
                List<RegiaoDistrito.Distrito> distritoList  = getDistritos(lojas);
                RegiaoDistrito regiaoDistrito = RegiaoDistrito.builder().idRegiao(informacaoLoja.getRegiao()).nomeRegiao(informacaoLoja.getDescricaoRegiao()).distritos(distritoList).lojas(lojaMap).build();
                regiaoDistritos.add(regiaoDistrito);
            });

        }

        return regiaoDistritos;
    }



    @Override
    public List<RegiaoDistritoCidade> buscarLojaPorRegiaoEDistrito(String regiaoId, String distritoId, String tipoLoja) {

        Assert.notNull(regiaoId, "Nao foi localizado o codigo da regiao para consultar a relacao de lojas");
        Assert.notNull(distritoId, "Nao foi localizado o codigo da distrito para consultar a relacao de lojas");

        Map<String, Object> metadata = getMetadataByParam(tipoLoja, regiaoId, distritoId);

        Loja lojaExample = Loja.builder().metadata(metadata).build();

        Example<Loja> example = Example.of(lojaExample);

        final List<Loja> allByRegiao = lojaRepository.findAll(example);

        List<RegiaoDistritoCidade> regiaoDistritos = new ArrayList<>();

        if (allByRegiao != null && !allByRegiao.isEmpty()) {

            //Agrupa as lojas por regiao
            Map<String, List<Loja>> distritoList = allByRegiao.stream().collect(Collectors.groupingBy(o -> String.valueOf(((InformacaoLoja) o.getMetadata().get("adicional")).getDistrito())));

            distritoList.forEach((distrito, lojas) -> {

                List<RegiaoDistritoCidade.Cidade> cidadeList = lojas.stream().
                        filter(loja -> ((InformacaoLoja) loja.getMetadata().get("adicional")).getDistrito().equalsIgnoreCase(distrito)).
                        map(this::extractInformacaoLoja).map(this::mapearNomeCidade).collect(Collectors.toList());

                List<Map<String, Object>> lojasMap  = getLojas(lojas);

                Loja loja = lojas.get(0);

                if (loja.getMetadata().get("adicional") instanceof InformacaoLoja) {

                        InformacaoLoja informacaoAdicional = (InformacaoLoja) loja.getMetadata().get("adicional");
                        String idDistrito   = informacaoAdicional.getDistrito();
                        String nomeDistrito = informacaoAdicional.getDescricaoDistrito();
                        String nomeRegiao   = informacaoAdicional.getDescricaoRegiao();
                        String idRegiao     = informacaoAdicional.getRegiao();

                        RegiaoDistritoCidade cidade = RegiaoDistritoCidade.builder().
                                idRegiao(idRegiao).
                                nomeRegiao(nomeRegiao).
                                idDistrito(idDistrito).
                                nomeDistrito(nomeDistrito).
                                lojas(lojasMap).
                                cidades(cidadeList).
                                build();

                    regiaoDistritos.add(cidade);
                }

            });
        }

        return regiaoDistritos;
    }

    private Map<String, Object> getMetadataByParam(String tipoLoja, String regiaoId) {
        Map<String, Object> metadata = getMetadataByParam(tipoLoja, regiaoId, null, null);
        return metadata;
    }

    private Map<String, Object> getMetadataByParam(String tipoLoja, String regiaoId, String distritoId) {
        Map<String, Object> metadata = getMetadataByParam(tipoLoja, regiaoId, distritoId, null);
        return metadata;
    }

    private Map<String, Object> getMetadataByParam(String tipoLoja, String regiaoId, String distritoId, String cidade) {
        Map<String, Object> metadata = new HashMap();
        Map<String, Object> params = new HashMap();

        params.put("regiao", regiaoId);
        params.put("distrito", distritoId);
        params.put("nomeTipo", tipoLoja);
        params.put("cidade", cidade);

        if (Objects.isNull(tipoLoja)) {
            params.remove("nomeTipo", tipoLoja);
        }

        if (Objects.isNull(regiaoId) || regiaoId.equalsIgnoreCase("*") || regiaoId.equalsIgnoreCase("Todas") || regiaoId.equalsIgnoreCase("Todos")) {
            params.remove("regiao");
        }

        if (Objects.isNull(distritoId)  || distritoId.equalsIgnoreCase("*") || distritoId.equalsIgnoreCase("Todas") || distritoId.equalsIgnoreCase("Todos")) {
            params.remove("distrito");
        }

        if (Objects.isNull(cidade) || cidade.equalsIgnoreCase("*") || cidade.equalsIgnoreCase("Todas") || cidade.equalsIgnoreCase("Todos")) {
            params.remove("cidade");
        }

        metadata.put("adicional", params);
        return metadata;
    }


    public List<RegiaoDistritoCidadeLoja> buscarLojaPorRegiaoEDistritoECidade(String regiaoId, String distritoId, String cidadeId, String tipoLoja) {
        Assert.notNull(regiaoId, "Nao foi localizado o codigo da regiao para consultar a relacao de lojas");
        Assert.notNull(distritoId, "Nao foi localizado o codigo da distrito para consultar a relacao de lojas");

        Map<String, Object> metadata = getMetadataByParam(tipoLoja, regiaoId, distritoId,cidadeId);

        Loja lojaExample = Loja.builder().metadata(metadata).build();

        Example<Loja> example = Example.of(lojaExample);

        final List<Loja> allByRegiaoAndDistritoAndCidade = lojaRepository.findAll(example);

        List<RegiaoDistritoCidadeLoja> cidadeLojaList = new ArrayList();

        if (allByRegiaoAndDistritoAndCidade != null && !allByRegiaoAndDistritoAndCidade.isEmpty()) {

            Map<String, List<Loja>> cidadeMap = allByRegiaoAndDistritoAndCidade.stream().collect(Collectors.groupingBy(o -> String.valueOf(((InformacaoLoja) o.getMetadata().get("adicional")).getCidade())));

            cidadeMap.forEach((cidade, lojas) -> {
                lojas.forEach(loja -> {
                    /*Supplier<Stream<Loja>> stream = () -> Stream.of(allByRegiaoAndDistritoAndCidade.toArray(new Loja[allByRegiaoAndDistritoAndCidade.size()]));
                    List<Map<String, Object>> lojaMap        = getLojas(stream.get());
                    String idDistrito               = getValue(stream.get(), "$.metadata.adicional.id_distrito");
                    String nomeDistrito             = getValue(stream.get(), "$.metadata.adicional.desc_distrito");
                    String idRegiao                 = getValue(stream.get(), "$.metadata.adicional.regiao");
                    String nomeRegiao               = getValue(stream.get(), "$.metadata.adicional.nome_regiao");
                    String cidade                   = getValue(stream.get(), "$.metadata.adicional.cidade");*/

                    List<Map<String, Object>> lojaMap = getLojas(lojas);

                    if (loja.getMetadata().get("adicional") instanceof InformacaoLoja){
                        InformacaoLoja informacaoAdicional = (InformacaoLoja) loja.getMetadata().get("adicional");
                        String idDistrito = informacaoAdicional.getDistrito();
                        String nomeDistrito = informacaoAdicional.getDescricaoDistrito();
                        String nomeRegiao = informacaoAdicional.getDescricaoRegiao();
                        String idRegiao = informacaoAdicional.getRegiao();
                        RegiaoDistritoCidadeLoja cidadeLoja  = RegiaoDistritoCidadeLoja.builder().idRegiao(idRegiao).nomeRegiao(nomeRegiao).idDistrito(idDistrito).nomeDistrito(nomeDistrito).cidade(cidade).lojas(lojaMap).build();
                        cidadeLojaList.add(cidadeLoja);
                    }
                });
            });
        }
        return cidadeLojaList;
    }

    private List<Map<String, Object>> getLojas(Collection<Loja> lojaStream) {
        return lojaStream.stream().map(LojaServiceImpl::mapearInformacaoLoja).collect(Collectors.toList());
    }

    private InformacaoLoja extractInformacaoLoja(Loja loja) {
        if (loja.getMetadata().containsKey("adicional")) {
            Object informacaoLoja = loja.getMetadata().get("adicional");

            if (informacaoLoja instanceof InformacaoLoja){
                return ((InformacaoLoja) informacaoLoja);
            }
        }
        return new InformacaoLoja();
    }

    private List<RegiaoDistrito.Distrito> getDistritos(List<Loja> lojas) {
        Set<RegiaoDistrito.Distrito> collect = lojas.stream().map(this::extractInformacaoLoja).map(informacaoLoja -> {
            RegiaoDistrito.Distrito distrito = new RegiaoDistrito.Distrito();
            distrito.setId(informacaoLoja.getDistrito());
            distrito.setName(informacaoLoja.getDescricaoDistrito());
            return distrito;
        }).collect(Collectors.toSet());
        return new ArrayList<>(collect);
    }

    @Override
    public Page<Loja> findAll(Pageable pageable) {
        return lojaRepository.findAll(pageable);
    }

    @Override
    public void carregarDadosLoja() {

    }
}
