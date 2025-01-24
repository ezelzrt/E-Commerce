package ar.uba.fi.ingsoft1.masterProducts.controller;

import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProduct;
import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProductDTO;
import ar.uba.fi.ingsoft1.masterProducts.service.MasterProductService;
import ar.uba.fi.ingsoft1.attributes.repository.Attribute;
import ar.uba.fi.ingsoft1.attributes.repository.AttributeValue;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/masterProducts")
@RequiredArgsConstructor
public class MasterProductController {
    private final MasterProductService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MasterProduct createMasterProduct(@RequestBody final AddRequest request) {
        return service.createMasterProduct(request);
    }

    @GetMapping
    public List<MasterProduct> getMasterPrducts() {
        return service.getMasterProducts();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MasterProductDTO> getMasterProductById(@PathVariable Long id) {
        return service.getMasterProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{id}/attributes")
    public ResponseEntity<Set<Attribute>> getAttributesByMasterProductId(@PathVariable Long id) {
        return service.getAttributesByMasterProductId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
//
//    @GetMapping("/{id}/attributesValues")
//    public ResponseEntity<Set<AttributeValue>> getAttributesValuesByMasterProductId(@PathVariable Long id) {
//        return service.getAttributesValuesByMasterProductId(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//    }

}
