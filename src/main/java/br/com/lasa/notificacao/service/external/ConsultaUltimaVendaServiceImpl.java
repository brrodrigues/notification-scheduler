package br.com.lasa.notificacao.service.external;

import br.com.lasa.notificacao.domain.UltimaVendaLoja;
import br.com.lasa.notificacao.repository.exception.NoDataFoundException;
import br.com.lasa.notificacao.repository.external.InfoPDVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultaUltimaVendaServiceImpl implements ConsultaUltimaVendaService {

    @Autowired
    private InfoPDVRepository infoPDVRepository;

    @Override
    public UltimaVendaLoja buscarUltimaVenda(String loja) throws NoDataFoundException {
        return infoPDVRepository.buscarUltimaVenda(loja);
    }
}
