package br.com.lasa.notificacao.rest.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChildrenContent {

    private String description;
    private String name;
    private String id;
    private Link link;

}
