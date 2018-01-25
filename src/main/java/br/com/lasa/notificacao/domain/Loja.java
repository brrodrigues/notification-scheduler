package br.com.lasa.notificacao.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "Cadastro_Lojas")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loja {

    @Id
    private String id;
    private String nomeLoja;
    private String responsavelGeral;
    private Date horaAbertura;
    private Date horaFechamento;
    @Version
    private Long versao;
    @CreatedDate
    private Date criadoEm;
    @CreatedBy
    private String criadoPor;
    @LastModifiedDate
    private Date modificadoEm;
    @LastModifiedBy
    private String modificadoPor;

}
