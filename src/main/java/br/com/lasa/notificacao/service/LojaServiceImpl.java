package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Horario;
import br.com.lasa.notificacao.domain.Loja;
import br.com.lasa.notificacao.repository.LojaRepository;
import br.com.lasa.notificacao.service.external.CalendarioDeLojaService;
import br.com.lasa.notificacao.service.external.response.InformacaoLoja;
import br.com.lasa.notificacao.util.TimeFormatterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class LojaServiceImpl implements LojaService {

    @Autowired
    private LojaRepository lojaRepository;

    @Autowired
    private CalendarioDeLojaService calendarioDeLojaService;

    @Autowired
    private TimeFormatterUtils timeFormatterUtils;

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

        lojaId.setHorarios(Arrays.asList(
                Horario.builder().dia("SEG").abertura(loja.getHoraAbertura()).fechamento(loja.getHoraFechamento()).build(),//SEG
                Horario.builder().dia("TER").abertura(loja.getHoraAbertura()).fechamento(loja.getHoraFechamento()).build(),//TER
                Horario.builder().dia("QUA").abertura(loja.getHoraAbertura()).fechamento(loja.getHoraFechamento()).build(),//QUA
                Horario.builder().dia("QUI").abertura(loja.getHoraAbertura()).fechamento(loja.getHoraFechamento()).build(),//QUI
                Horario.builder().dia("SEX").abertura(loja.getHoraAbertura()).fechamento(loja.getHoraFechamento()).build(),//SEX
                Horario.builder().dia("SAB").abertura(loja.getHoraAbertura()).fechamento(loja.getHoraFechamento()).build(),//SAB
                Horario.builder().dia("DOM").abertura(loja.getHoraAbertura()).fechamento(loja.getHoraFechamento()).build()//DOM
                )
        );

        Loja save = lojaRepository.save(lojaId);
        return save;
    }

    private List<Horario> montarQuadroHorario(String lojaId){
        Collection<InformacaoLoja> informacaoLojas = calendarioDeLojaService.get(lojaId);
        List<Horario> horarios = informacaoLojas.stream().map(this::preencherLoja).collect(Collectors.toList());
        return horarios;

    }

    private Horario preencherLoja(InformacaoLoja informacaoVendaLoja) {

        Date dateAbertura = atribuirNoDiaDeSemanaCorrenteOHorarioInformado(informacaoVendaLoja.getDiaSemana(), informacaoVendaLoja.getHoraAbertura());
        Date dateFechamento = atribuirNoDiaDeSemanaCorrenteOHorarioInformado(informacaoVendaLoja.getDiaSemana(), informacaoVendaLoja.getHoraFechamento());

        Horario horario = Horario.builder().abertura(dateAbertura).fechamento(dateFechamento).dia(informacaoVendaLoja.getDiaExtensoSemana()).build();

        return horario;
    }

    public Loja atualizar(Loja loja){
        //Atribui o horario de abertura e fechamento para todos os dias ao atualizar

        if (loja == null) {
            throw new IllegalArgumentException("Nao foi possivel localizar os dados da loja para serem atualizados. Verifique as informacoes enviadas para o servidor.");
        }

        loja.setHorarios(Arrays.asList(
                Horario.builder().dia("SEG").abertura(loja.getHoraAbertura()).fechamento(loja.getHoraFechamento()).build(),//SEG
                Horario.builder().dia("TER").abertura(loja.getHoraAbertura()).fechamento(loja.getHoraFechamento()).build(),//TER
                Horario.builder().dia("QUA").abertura(loja.getHoraAbertura()).fechamento(loja.getHoraFechamento()).build(),//QUA
                Horario.builder().dia("QUI").abertura(loja.getHoraAbertura()).fechamento(loja.getHoraFechamento()).build(),//QUI
                Horario.builder().dia("SEX").abertura(loja.getHoraAbertura()).fechamento(loja.getHoraFechamento()).build(),//SEX
                Horario.builder().dia("SAB").abertura(loja.getHoraAbertura()).fechamento(loja.getHoraFechamento()).build(),//SAB
                Horario.builder().dia("DOM").abertura(loja.getHoraAbertura()).fechamento(loja.getHoraFechamento()).build()//DOM
                )
        );

        lojaRepository.delete(loja.getId());
        Loja save = lojaRepository.save(loja);
        return save;
    }

    private Date atribuirNoDiaDeSemanaCorrenteOHorarioInformado(Integer diaSemana, String horario) {

        LocalTime horarioAbertura = timeFormatterUtils.toTime(horario);
        Calendar instance = Calendar.getInstance(context.getBean(TimeZone.class));
        instance.set(Calendar.DAY_OF_WEEK, diaSemana);

        LocalDateTime dataReferenteAoDiaSemana = LocalDateTime.of(instance.getTime().toInstant().atZone(zoneId).toLocalDate(), horarioAbertura);

        Date dataFinalAbertura = Date.from(dataReferenteAoDiaSemana.atZone(zoneId).toInstant());

        return dataFinalAbertura;

    }
}
