package br.com.lasa.notificacao.rest.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParentContent {

    private String selfType;
    private String selfName;
    private String selfId;
    private Set<ChildrenContent> children;
    private Collection<Map<String,Object>> lojas;


}
