package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Horario;
import br.com.lasa.notificacao.domain.Loja;
import br.com.lasa.notificacao.repository.LojaRepository;
import br.com.lasa.notificacao.service.external.CalendarioDeLojaExternalService;
import br.com.lasa.notificacao.service.external.response.InformacaoLoja;
import br.com.lasa.notificacao.util.DateTimeFormatterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.ZoneId;
import java.util.Collection;
import java.util.List;

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

    public Loja atualizar(String id, Loja loja) {
        //Atribui o horario de abertura e fechamento para todos os dias ao atualizar
        Loja lojaId = lojaRepository.findOne(id);

        lojaId = loja;
        lojaId.setId(id);

        if (lojaId == null) {
            throw new IllegalArgumentException("Nao foi possivel localizar os dados da loja para serem atualizados. Verifique as informacoes enviadas para o servidor.");
        }

        Collection<InformacaoLoja> informacaoLojas = calendarioDeLojaExternalService.get(id);
        List<Horario> horarios = calendarioDeLojaExternalService.montarQuadroDeHorario(informacaoLojas);

        lojaId.setHorarios(horarios);

        Loja save = lojaRepository.save(lojaId);
        return save;
    }

   public Loja atualizar(Loja loja){
        //Atribui o horario de abertura e fechamento para todos os dias ao atualizar
        Assert.isNull(loja, "Nao foi possivel localizar os dados da loja para serem atualizados. Verifique as informacoes enviadas para o servidor.");

        Collection<InformacaoLoja> informacaoLojas = calendarioDeLojaExternalService.get(loja.getId());

        loja.setHorarios(calendarioDeLojaExternalService.montarQuadroDeHorario(informacaoLojas));

        lojaRepository.delete(loja.getId());
        Loja save = lojaRepository.save(loja);
        return save;
   }


}
