package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Loja;
import br.com.lasa.notificacao.repository.LojaRepository;
import br.com.lasa.notificacao.service.external.CalendarioDeLojaExternalService;
import br.com.lasa.notificacao.service.external.response.CalendarioDeLoja;
import br.com.lasa.notificacao.util.DateTimeFormatterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.ZoneId;

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


}
