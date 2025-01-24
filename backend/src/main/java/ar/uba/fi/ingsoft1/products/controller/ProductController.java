package ar.uba.fi.ingsoft1.products.controller;

import ar.uba.fi.ingsoft1.auth.service.JwtService;
import ar.uba.fi.ingsoft1.exception.ExpiredTokenException;
import ar.uba.fi.ingsoft1.exception.InvalidTokenException;
import ar.uba.fi.ingsoft1.exception.ProductAlreadyExistsException;
import ar.uba.fi.ingsoft1.products.repository.Product;
import ar.uba.fi.ingsoft1.products.repository.ProductDTO;
import ar.uba.fi.ingsoft1.products.service.ProductService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;
    private final JwtService jwtService;
    private List<ProductDTO> productsAsProductDTO(List<Product> products) {
    return products.stream()
        .map(ProductDTO::new)
        .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createProduct(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody final CreateProductDTO createProductDTO) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            jwtService.validateToken(token);

            int accessType = jwtService.extractAccessType(token);

            if (accessType != 1)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized", "message", "You do not have the required access"));

            Product createdProduct = service.createProduct(createProductDTO);
            ProductDTO productDTO = new ProductDTO(createdProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
        } catch (InvalidTokenException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized", "message", ex.getMessage()));
        } catch (ExpiredTokenException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Bad Request", "message", ex.getMessage()));
        } catch (ProductAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Conflict", "message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal Server Error", "message", ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.deleteProductById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductDTO>> getProducts() {
        List<Product> products = service.getProducts();
        List<ProductDTO> productDTOs = productsAsProductDTO(products);
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return service.getProductById(id)
                .map(product -> ResponseEntity.ok(new ProductDTO(product)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> getProductsByMasterId(@RequestParam(name = "masterProduct") Long masterId) 
    {
        Optional<List<Product>> products = service.getProductsByMasterId(masterId);

        if (products.isPresent()) {
            List<ProductDTO> productDTOs = productsAsProductDTO(products.get());
            return ResponseEntity.ok(productDTOs);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
    @GetMapping("/{id}/stock")
    public ResponseEntity<Long> getStockProductById(@PathVariable Long id) {
        return service.getStockProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Long> patchStockProduct(@PathVariable Long id, @RequestBody Map<String, Long> requestBody) {
        Long addStock = requestBody.get("addStock");
        return service.patchStockProduct(id, addStock)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}/compromisedStock")
    public ResponseEntity<Map<String,Long>> patchCompromisedStockProduct(@PathVariable Long id, @RequestBody Map<String, Long> requestBody) {
        Long addCompromisedStock = requestBody.get("addCompromisedStock");
        return service.patchCompromisedStockProduct(id, addCompromisedStock)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
