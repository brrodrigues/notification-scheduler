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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

            //Agrupa as lojas por regiac
            Map<String, List<Loja>> regiao = allByRegiao.stream().collect(Collectors.groupingBy(o -> String.valueOf(((InformacaoLoja) o.getMetadata().get("adicional")).getRegiao())));

            regiao.forEach((s, lojas) -> {
                Supplier<Stream<Loja>> stream = () -> Stream.of(lojas.toArray(new Loja[lojas.size()]));
                List<Map<String, Object>> lojaMap  = getLojas(stream.get());
                Collection<String> distritoList    = getListFromJsonPath(stream.get(), "$.metadata.adicional.id_distrito");
                String nomeRegiao                  = getValue(stream.get(), "$.metadata.adicional.nome_regiao");
                String idRegiao                    = getValue(stream.get(), "$.metadata.adicional.regiao");
                RegiaoDistrito regiaoDistrito = RegiaoDistrito.builder().idRegiao(idRegiao).nomeRegiao(nomeRegiao).distritos(distritoList).lojas(lojaMap).build();
                regiaoDistritos.add(regiaoDistrito);
            });

        }

        return regiaoDistritos;
    }



    @Override
    public RegiaoDistritoCidade buscarLojaPorRegiaoEDistrito(String regiaoId, String distritoId, String tipoLoja) {

        Assert.notNull(regiaoId, "Nao foi localizado o codigo da regiao para consultar a relacao de lojas");
        Assert.notNull(distritoId, "Nao foi localizado o codigo da distrito para consultar a relacao de lojas");

        Map<String, Object> metadata = getMetadataByParam(tipoLoja, regiaoId, distritoId);

        Loja lojaExample = Loja.builder().metadata(metadata).build();

        Example<Loja> example = Example.of(lojaExample);

        final List<Loja> allByRegiao = lojaRepository.findAll(example);

        final List<Loja> allByRegiaoAndDistrito = lojaRepository.findAll(example);

        if (allByRegiaoAndDistrito != null && !allByRegiaoAndDistrito.isEmpty()) {

            Supplier<Stream<Loja>> stream = () -> Stream.of(allByRegiaoAndDistrito.toArray(new Loja[allByRegiaoAndDistrito.size()]));

            List<Map<String, Object>> lojas      = getLojas(stream.get());
            Collection<String> cidadeList      = getListFromJsonPath(stream.get(), "$.metadata.adicional.cidade");
            String idDistrito             = getValue(stream.get(), "$.metadata.adicional.id_distrito");
            String regiao             = getValue(stream.get(), "$.metadata.adicional.regiao");
            String nomeRegiao             = getValue(stream.get(), "$.metadata.adicional.nome_regiao");
            String nomeDistrito             = getValue(stream.get(), "$.metadata.adicional.desc_distrito");

            RegiaoDistritoCidade regiaoDistritoCidade = RegiaoDistritoCidade.builder().idRegiao(regiao).nomeRegiao(nomeRegiao).lojas(lojas).idDistrito(idDistrito).cidades(cidadeList).nomeDistrito(nomeDistrito).build();

            return regiaoDistritoCidade;
        }

        return new RegiaoDistritoCidade();
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


    public RegiaoDistritoCidadeLoja buscarLojaPorRegiaoEDistritoECidade(String regiaoId, String distritoId, String cidadeId, String tipoLoja) {
        Assert.notNull(regiaoId, "Nao foi localizado o codigo da regiao para consultar a relacao de lojas");
        Assert.notNull(distritoId, "Nao foi localizado o codigo da distrito para consultar a relacao de lojas");

        Map<String, Object> metadata = getMetadataByParam(tipoLoja, regiaoId, distritoId,cidadeId);

        Loja lojaExample = Loja.builder().metadata(metadata).build();

        Example<Loja> example = Example.of(lojaExample);

        final List<Loja> allByRegiaoAndDistritoAndCidade = lojaRepository.findAll(example);

        if (allByRegiaoAndDistritoAndCidade != null && !allByRegiaoAndDistritoAndCidade.isEmpty()) {

            Supplier<Stream<Loja>> stream = () -> Stream.of(allByRegiaoAndDistritoAndCidade.toArray(new Loja[allByRegiaoAndDistritoAndCidade.size()]));

            List<Map<String, Object>> lojas        = getLojas(stream.get());
            String idDistrito               = getValue(stream.get(), "$.metadata.adicional.id_distrito");
            String nomeDistrito             = getValue(stream.get(), "$.metadata.adicional.desc_distrito");
            String idRegiao                 = getValue(stream.get(), "$.metadata.adicional.regiao");
            String nomeRegiao               = getValue(stream.get(), "$.metadata.adicional.nome_regiao");
            String cidade                   = getValue(stream.get(), "$.metadata.adicional.cidade");
            RegiaoDistritoCidadeLoja loja   = RegiaoDistritoCidadeLoja.builder().idRegiao(idRegiao).nomeRegiao(nomeRegiao).idDistrito(idDistrito).nomeDistrito(nomeDistrito).cidade(cidade).lojas(lojas).build();

            return loja;
        }
        return new RegiaoDistritoCidadeLoja();
    }

    private List<Map<String, Object>> getLojas(Stream<Loja> lojaStream) {
        return lojaStream.map(LojaServiceImpl::mapearInformacaoLoja).collect(Collectors.toList());
    }

    private Collection<String> getListFromJsonPath(Stream<Loja> lojas, String jsonPath) {
        List<String> collect = lojas.map(loja -> (String) JsonMap.searchFromXPath(loja, jsonPath)).collect(Collectors.toList());
        return collect;
    }

    /**
     * Retorna o nome da regiao
     * @param stream Lista de Lojas
     * @param jsonPath Expressao em Json para localizar o retorno da informacao
     * @return
     */
    private String getValue(Stream<Loja> stream, String jsonPath){
        String nomeRegiao = "";

        Optional<Loja> primeiraLoja = stream.findFirst();
        if (primeiraLoja.isPresent()){

            Loja loja = primeiraLoja.get();

            nomeRegiao = (String) JsonMap.searchFromXPath(loja, jsonPath);

        }
        return nomeRegiao;
    }

    @Override
    public Page<Loja> findAll(Pageable pageable) {
        return lojaRepository.findAll(pageable);
    }

    @Override
    public void carregarDadosLoja() {

    }
}
