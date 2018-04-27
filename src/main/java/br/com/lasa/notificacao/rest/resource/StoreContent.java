package br.com.lasa.notificacao.rest.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StoreContent {

    private String label;
    private String value;
    private String storeType;

}
