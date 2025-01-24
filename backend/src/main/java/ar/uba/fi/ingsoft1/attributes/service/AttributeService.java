package ar.uba.fi.ingsoft1.attributes.service;

import ar.uba.fi.ingsoft1.attributes.controller.AddRequest;
import ar.uba.fi.ingsoft1.attributes.repository.Attribute;
import ar.uba.fi.ingsoft1.attributes.repository.AttributeRepository;
import ar.uba.fi.ingsoft1.orders.repository.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttributeService {
    private final AttributeRepository attributeRepository;
    public void add(AddRequest request){
        var product = Attribute.builder()
                .build();
        var savedProduct = attributeRepository.save(product);
        return;
    }

    public List<Attribute> getAttributes() {
        return attributeRepository.findAll();
    }

}
