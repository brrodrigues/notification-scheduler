package br.com.lasa.notificacao.domain.document;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
public class Permission implements Serializable {

    private String id;
    private List<String> httpMethod;
    private List<Feature> features;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(List<String> httpMethod) {
        this.httpMethod = httpMethod;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}
