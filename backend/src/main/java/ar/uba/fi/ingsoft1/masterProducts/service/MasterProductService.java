package ar.uba.fi.ingsoft1.masterProducts.service;

import ar.uba.fi.ingsoft1.masterProducts.controller.AddRequest;
import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProduct;
import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProductDTO;
import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProductRepository;
import ar.uba.fi.ingsoft1.attributes.repository.Attribute;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MasterProductService {
    MasterProductRepository masterProductRepository;

    @Autowired
    public void MasterProductController(MasterProductRepository masterProductRepository) {
        this.masterProductRepository = masterProductRepository;
    }

    public MasterProduct createMasterProduct(AddRequest request){
        var product = MasterProduct.builder()
                .build();
        var savedProduct = masterProductRepository.save(product);
        return savedProduct;
    }

    public List<MasterProduct> getMasterProducts(){
        List<MasterProduct> products = masterProductRepository.findAll();
        return products;
    }

    public Optional<MasterProductDTO> getMasterProductById(Long id) {
        return findMasterProductById(id);
    }

    public Optional<Set<Attribute>> getAttributesByMasterProductId(Long id) {
        return masterProductRepository.findById(id).map(mp -> mp.getAttributes());
    }

    private Optional<MasterProductDTO> findMasterProductById(Long id) {
        return Optional.of(new MasterProductDTO(masterProductRepository.findById(id).get()));
    }
}
