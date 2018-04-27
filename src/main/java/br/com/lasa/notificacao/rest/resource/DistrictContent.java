package br.com.lasa.notificacao.rest.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictContent {


    private String type;
    private String label;
    private String value;
    private Set<StoreContent> children = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DistrictContent that = (DistrictContent) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(label, that.label) &&
                Objects.equals(value, that.value);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<StoreContent> getChildren() {
        return children;
    }

    public void setChildren(Set<StoreContent> children) {
        this.children = children;
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, label, value);
    }
}
