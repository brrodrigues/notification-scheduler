package br.com.lasa.notificacao.repository.external;

import br.com.lasa.notificacao.domain.UltimaVendaLoja;
import br.com.lasa.notificacao.repository.exception.MysqlNoDataFoundException;

public interface InfoPDVRepository {

    public UltimaVendaLoja buscarUltimaVenda(String store) throws MysqlNoDataFoundException;

}
