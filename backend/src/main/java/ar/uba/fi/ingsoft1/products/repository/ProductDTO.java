package ar.uba.fi.ingsoft1.products.repository;

import ar.uba.fi.ingsoft1.attributes.repository.AttributeValue;
import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProduct;
import lombok.Data;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Data
public class ProductDTO {
    private Long id;
    private MasterProductDTO masterproduct;
    private String name;
    private Long stock;
    private String imageURL;
    private ArrayList<AttributeValueDTO> attributeValues;

    public ProductDTO(Product product){
        this.id = product.getId();
        this.masterproduct = new MasterProductDTO(product.getMasterProduct());
        this.name = product.getName();
        this.stock = product.getStock();
        this.imageURL = "/images/" + product.getImageUrl();
        this.attributeValues = product.getAttributeValues()
                .stream()
                .map(AttributeValueDTO::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Data
    public class MasterProductDTO {
        private Long id;
        private String name;
        private String description;
        private double basePrice;

        public MasterProductDTO(MasterProduct masterProduct) {
            this.id = masterProduct.getId();
            this.name = masterProduct.getName();
            this.description = masterProduct.getDescription();
            this.basePrice = masterProduct.getBasePrice();
        }
    }

    @Data
    public class AttributeValueDTO {
        private Long id;
        private String value;
        private String name;

        public AttributeValueDTO(AttributeValue attributeValue) {
            this.id = attributeValue.getId();
            this.value = attributeValue.getValue();
            this.name = attributeValue.getAttribute().getName();
        }
    }


}
