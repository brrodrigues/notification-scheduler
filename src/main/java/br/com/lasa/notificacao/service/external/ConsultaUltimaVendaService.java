package br.com.lasa.notificacao.service.external;

import br.com.lasa.notificacao.domain.UltimaVendaLoja;
import br.com.lasa.notificacao.repository.exception.NoDataFoundException;

public interface ConsultaUltimaVendaService {

    public UltimaVendaLoja buscarUltimaVenda(String loja) throws NoDataFoundException;

}
