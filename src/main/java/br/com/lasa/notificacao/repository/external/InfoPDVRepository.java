package br.com.lasa.notificacao.repository.external;

import br.com.lasa.notificacao.domain.UltimaVendaLoja;
import br.com.lasa.notificacao.repository.exception.NoDataFoundException;

public interface InfoPDVRepository {

    public UltimaVendaLoja buscarUltimaVenda(String store) throws NoDataFoundException;

}
