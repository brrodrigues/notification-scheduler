package br.com.lasa.notificacao.service;

import br.com.lasa.notificacao.domain.Horario;
import br.com.lasa.notificacao.domain.Loja;
import br.com.lasa.notificacao.repository.LojaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class LojaServiceImpl implements LojaService {

    @Autowired
    private LojaRepository lojaRepository;

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
}
