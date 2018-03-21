package br.com.lasa.notificacao.rest.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChildrenContent {

    private String childName;
    private Set<Map<String, Object>> childData;

}
