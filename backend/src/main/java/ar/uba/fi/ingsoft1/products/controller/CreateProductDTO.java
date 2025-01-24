package ar.uba.fi.ingsoft1.products.controller;

import lombok.Data;

import java.util.List;

@Data
public class CreateProductDTO {
    private Long masterproductid;
    private String name;
    private Long stock;

    private List<AttributeValueDTO> attributes;

    @Data
    public static class AttributeValueDTO {
        private Long id;
        private Long value;
    }
}
