package ar.uba.fi.ingsoft1.products.service;

import ar.uba.fi.ingsoft1.attributes.repository.AttributeValue;
import ar.uba.fi.ingsoft1.attributes.repository.AttributeValueRepository;
import ar.uba.fi.ingsoft1.exception.ProductAlreadyExistsException;
import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProduct;
import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProductRepository;
import ar.uba.fi.ingsoft1.products.controller.CreateProductDTO;
import ar.uba.fi.ingsoft1.products.repository.Product;
import ar.uba.fi.ingsoft1.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    final private ProductRepository productRepository;
    final private MasterProductRepository masterProductRepository;
    final private AttributeValueRepository attributeValueRepository;
    
    public Product createProduct(CreateProductDTO createProductDTO){
        MasterProduct masterProduct = masterProductRepository.findById(createProductDTO.getMasterproductid())
                .orElseThrow(() -> new RuntimeException("Master Product not found"));

        Optional<List<Product>> existingProducts = productRepository.findByMasterProduct(masterProduct);

        for (Product existingProduct : existingProducts.get()) {
            List<AttributeValue> existingAttributes = existingProduct.getAttributeValues();
            List<AttributeValue> newAttributes = createProductDTO.getAttributes().stream()
                    .map(attr -> attributeValueRepository.findById(attr.getValue()).get())
                    .collect(Collectors.toList());

            existingAttributes.sort(Comparator.comparing(AttributeValue::getId));
            newAttributes.sort(Comparator.comparing(AttributeValue::getId));

            if (existingAttributes.size() == newAttributes.size()) {
                boolean attributesMatch = true;

                // Comparamos cada par de atributos en ambas listas
                for (int i = 0; i < existingAttributes.size(); i++) {
                    AttributeValue existing = existingAttributes.get(i);
                    AttributeValue newAttr = newAttributes.get(i);

                    // Si algún atributo no coincide, se marca como no coincidente
                    if (!existing.getId().equals(newAttr.getId()) || !existing.getValue().equals(newAttr.getValue())) {
                        attributesMatch = false;
                        break;
                    }
                }

                // Si todos los atributos coinciden, lanzamos la excepción
                if (attributesMatch) {
                    throw new ProductAlreadyExistsException("A product with the same attributes already exists");
                }
            }
        }

        Product product = Product.builder()
                .masterProduct(masterProduct)
                .imageUrl("box.svg")
                .compromisedStock(0L)
                .stock(createProductDTO.getStock())
                .name(createProductDTO.getName())
                .price(masterProduct.getBasePrice())
                .attributeValues(
                        createProductDTO.getAttributes().stream().map(attributeValueDTO -> attributeValueRepository.findById(attributeValueDTO.getValue()).get()).collect(Collectors.toList())
                )
                .build();
        return productRepository.save(product);
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<List<Product>> getProductsByMasterId(Long masterId) {
        Optional<MasterProduct> masterProduct = masterProductRepository.findById(masterId);
        return productRepository.findByMasterProduct(masterProduct.get());
    }

    public Optional<Long> getStockProductById(Long id) {
        Optional<Product> optProduct = productRepository.findById(id);
        return optProduct.map(product -> product.getStock());
    }

    @SuppressWarnings("unchecked")
    public Optional<ResponseEntity<Long>> patchStockProduct(Long id, Long addStock) {
        Optional<Product> optProduct = productRepository.findById(id);
        return optProduct.map(product -> {
            Long newStock = product.addStock(addStock);
            try {
                productRepository.save(product);
            } catch (Exception e) {
                boolean cond = newStock < 0;
                return (ResponseEntity<Long>) stockErrorCase(e, cond);
            }
            
            return ResponseEntity.ok(newStock);
        });
    }

    /*
     * Actualiza el stock comprometido agregando el valor, negativo o positivo, de 'addCompromisedStock'.
     * Si stock o stock comprometido quedaran con valores negativos, se lanzara una excepción al 
     * intentar persistir los nuevos valores y se anulara la modificación.
     */
    @SuppressWarnings("unchecked")
    public Optional<ResponseEntity<Map<String, Long>>> patchCompromisedStockProduct(Long id, Long addCompromisedStock) {
        Optional<Product> optProduct = productRepository.findById(id);
        return optProduct.map(product -> {
            Long newCompromisedStock = product.addCompromisedStock(addCompromisedStock);
            Long newStock = product.getStock();
            try {
                productRepository.save(product);
            } catch (Exception e) {
                boolean cond = newStock < 0 || newCompromisedStock < 0;
                return (ResponseEntity<Map<String, Long>>) stockErrorCase(e, cond);
            }
            return ResponseEntity.ok(Map.of(
                "compromisedStock", newCompromisedStock,
                "stock", newStock
            ));
        });
    }

    private ResponseEntity<?> stockErrorCase(Exception e, boolean cond){
        if (cond) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .header("Error-Message", "Insufficient stock")
                            .body(null);
        };
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .header("Error-Message", e.getMessage())
                            .body(null);
    }

    public Double getProductPriceById(Long productId) {
        return productRepository.findById(productId)
            .map(p -> p.getPrice())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void updateCompromisedStockProductDelivered(Long productId, Long amountDelivered) {
        Product product = productRepository.findById(productId).get();
        Long newCompromisedStock = product.getCompromisedStock() - amountDelivered;
        product.setCompromisedStock(newCompromisedStock);
        productRepository.save(product);
    }


}
