package ar.uba.fi.ingsoft1.masterProducts.repository;
import java.util.stream.Collectors;

import ar.uba.fi.ingsoft1.attributes.repository.Attribute;
import ar.uba.fi.ingsoft1.attributes.repository.AttributeValue;
import ar.uba.fi.ingsoft1.orders.repository.OrderDetail;
import ar.uba.fi.ingsoft1.products.repository.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class MasterProductDTO {
    private Long id;
    private String name;
    private String description;
    private double basePrice;
    private List<ProductDTO> products;

    public MasterProductDTO(MasterProduct masterProduct) {
        this.id = masterProduct.getId();
        this.name = masterProduct.getName();
        this.description = masterProduct.getDescription();
        this.basePrice = masterProduct.getBasePrice();
        this.products = masterProduct.getProducts().stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    @Data
    public class ProductDTO {
        private Long id;
        private String name;
        private Double price;
        private Long availableStock;
        private String imageUrl;
        private List<AttributeValueDTO> attributeValues;

        public ProductDTO(Product product) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.availableStock = product.getStock() - product.getCompromisedStock();
            this.imageUrl = product.getImageUrl();
            this.attributeValues = product.getAttributeValues().stream().map(AttributeValueDTO::new).collect(Collectors.toList());
        }
    }

    @Data
    public class AttributeValueDTO {
        private Long id;
        private String value;
        private AttributeDTO attribute;

        public AttributeValueDTO(AttributeValue attributeValue){
            this.id = attributeValue.getId();
            this.value = attributeValue.getValue();
            this.attribute = new AttributeDTO(attributeValue.getAttribute());
        }
    }

    @Data class AttributeDTO {
        private Long id;
        private String name;

        public AttributeDTO(Attribute attribute){
            this.id = attribute.getId();
            this.name = attribute.getName();
        }
    }
}
