package br.com.lasa.notificacao.repository.external;

import br.com.lasa.notificacao.domain.UltimaVendaLoja;
import br.com.lasa.notificacao.repository.exception.MysqlNoDataFoundException;
import br.com.lasa.notificacao.util.AppConstants;
import br.com.lasa.notificacao.util.DaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class InfoPDVRepositoryImpl implements InfoPDVRepository {

    private static final String SQL = "select NOW(), TIMESTAMP(data, STR_TO_DATE(ultima_venda, '%H:%i:%s')), data 'dataVenda', STR_TO_DATE(ultima_venda, '%H:%i:%s') 'ultimaVenda' FROM pdv.INFOPDV_ULTIMA_VENDA where loja = ? order by dataVenda desc, ultimaVenda desc limit 1";

    @Autowired
    @Qualifier(value = AppConstants.FLASH_JDBC_TEMPLATE)
    private JdbcTemplate template;

    @Override
    public UltimaVendaLoja buscarUltimaVenda(String store) throws MysqlNoDataFoundException {
        List<UltimaVendaLoja> query = template.query(SQL, Collections.singletonList(store).toArray(), (rs, indexColumn) -> {
            UltimaVendaLoja ultimaVendaLoja = new UltimaVendaLoja(DaoUtil.getLocalDateTime(rs, 1), DaoUtil.getLocalDateTime(rs,2));
            return ultimaVendaLoja;
        });

        if (query != null && !query.isEmpty()){
            UltimaVendaLoja ultimaVendaLoja = query.get(0);
            return ultimaVendaLoja;
        }

        throw new MysqlNoDataFoundException("Nao foi encontrado venda");
    }
}
