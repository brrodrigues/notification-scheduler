package br.com.lasa.notificacao.rest.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionContent {


    private String type;
    private String label;
    private String value;
    private List<DistrictContent> children = new LinkedList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegionContent that = (RegionContent) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(label, that.label) &&
                Objects.equals(value, that.value);
    }

}
