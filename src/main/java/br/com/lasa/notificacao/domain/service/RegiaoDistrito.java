package br.com.lasa.notificacao.domain.service;

import lombok.*;

import java.util.List;
import java.util.Map;

@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegiaoDistrito {

    private String idRegiao;
    private String nomeRegiao;
    private List<Map<String, Object>> lojas;
    private List<Distrito> distritos;

    public static class Distrito {

        String name;
        String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
