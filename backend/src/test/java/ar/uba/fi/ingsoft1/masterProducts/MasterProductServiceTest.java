package ar.uba.fi.ingsoft1.masterProducts;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ar.uba.fi.ingsoft1.attributes.repository.Attribute;
import ar.uba.fi.ingsoft1.attributes.repository.AttributeValue;
import ar.uba.fi.ingsoft1.masterProducts.controller.AddRequest;
import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProduct;
import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProductDTO;
import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProductRepository;
import ar.uba.fi.ingsoft1.masterProducts.service.MasterProductService;
import ar.uba.fi.ingsoft1.products.repository.Product;

import java.util.*;

public class MasterProductServiceTest {

    @InjectMocks
    private MasterProductService masterProductService;

    @Mock
    private MasterProductRepository masterProductRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMasterProduct() {
        AddRequest request = new AddRequest();
        MasterProduct mockMasterProduct = MasterProduct.builder()
                .name("Product Name")
                .description("Product Description")
                .basePrice(100.0)
                .build();

        when(masterProductRepository.save(any(MasterProduct.class))).thenReturn(mockMasterProduct);

        MasterProduct createdProduct = masterProductService.createMasterProduct(request);

        assertNotNull(createdProduct);
        assertEquals("Product Name", createdProduct.getName());
        assertEquals(100.0, createdProduct.getBasePrice());
        verify(masterProductRepository, times(1)).save(any(MasterProduct.class));
    }

    @Test
    void testGetMasterProducts() {
        List<MasterProduct> mockProducts = Arrays.asList(
                MasterProduct.builder().id(1L).name("Product 1").build(),
                MasterProduct.builder().id(2L).name("Product 2").build()
        );
        when(masterProductRepository.findAll()).thenReturn(mockProducts);

        List<MasterProduct> products = masterProductService.getMasterProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Product 1", products.get(0).getName());
        verify(masterProductRepository, times(1)).findAll();
    }

    @Test
    void testGetMasterProductByIdFound() {
        Long id = 1L;

        List<AttributeValue> mockAttributeValues = List.of(
            AttributeValue.builder()
                    .id(1L)
                    .value("Red")
                    .attribute(Attribute.builder()
                            .id(1L)
                            .name("Color")
                            .build())
                    .build()
        );

        List<Product> mockProducts = List.of(
            Product.builder()
                    .id(1L)
                    .name("Mock Product")
                    .price(100.0)
                    .stock(10L)
                    .compromisedStock(2L)
                    .attributeValues(mockAttributeValues)
                    .build()
        );

        MasterProduct mockMasterProduct = MasterProduct.builder()
                .id(id)
                .name("Product Name")
                .products(mockProducts)
                .build();

        when(masterProductRepository.findById(id)).thenReturn(Optional.of(mockMasterProduct));

        Optional<MasterProductDTO> productDTO = masterProductService.getMasterProductById(id);

        assertTrue(productDTO.isPresent());
        assertEquals(id, productDTO.get().getId());
        assertEquals("Product Name", productDTO.get().getName());
        assertEquals(1, productDTO.get().getProducts().size());
        assertEquals("Mock Product", productDTO.get().getProducts().get(0).getName());
        assertEquals("Red", productDTO.get().getProducts().get(0).getAttributeValues().get(0).getValue());
    }

    @Test
    void testGetMasterProductByIdNotFound() {
        Long id = 1L;
    
        when(masterProductRepository.findById(id)).thenThrow(new NoSuchElementException("No value present"));
    
        assertThrows(NoSuchElementException.class, () -> masterProductService.getMasterProductById(id));
    }

    @Test
    void testGetAttributesByMasterProductId() {

        Long id = 1L;

        Attribute color = new Attribute();
        color.setId(1L);
        color.setName("Color");
        
        Attribute size = new Attribute();
        size.setId(2L);
        size.setName("Size");

        Set<Attribute> mockAttributes = Set.of(color, size);
        MasterProduct mockMasterProduct = MasterProduct.builder().id(id).attributes(mockAttributes).build();
        when(masterProductRepository.findById(id)).thenReturn(Optional.of(mockMasterProduct));

        Optional<Set<Attribute>> attributes = masterProductService.getAttributesByMasterProductId(id);

        assertTrue(attributes.isPresent());
        assertEquals(2, attributes.get().size());
        verify(masterProductRepository, times(1)).findById(id);
    }
}

