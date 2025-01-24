package ar.uba.fi.ingsoft1.products;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import ar.uba.fi.ingsoft1.products.service.ProductService;
import ar.uba.fi.ingsoft1.attributes.repository.AttributeValue;
import ar.uba.fi.ingsoft1.attributes.repository.AttributeValueRepository;
import ar.uba.fi.ingsoft1.exception.ProductAlreadyExistsException;
import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProduct;
import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProductRepository;
import ar.uba.fi.ingsoft1.products.controller.CreateProductDTO;
import ar.uba.fi.ingsoft1.products.repository.Product;
import ar.uba.fi.ingsoft1.products.repository.ProductRepository;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MasterProductRepository masterProductRepository;

    @Mock
    private AttributeValueRepository attributeValueRepository;

    @InjectMocks
    private ProductService productService;

    private MasterProduct mockMasterProduct;
    private Product mockProduct;
    private AttributeValue mockAttributeValue;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        masterProductRepository = mock(MasterProductRepository.class);
        attributeValueRepository = mock(AttributeValueRepository.class);

        productService = new ProductService(productRepository, masterProductRepository, attributeValueRepository);

        mockMasterProduct = new MasterProduct();
        mockMasterProduct.setId(1L);
        mockMasterProduct.setName("Master Product");
        mockMasterProduct.setBasePrice(100.0);

        mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Test Product");
        mockProduct.setMasterProduct(mockMasterProduct);
        mockProduct.setStock(10L);
        mockProduct.setPrice(mockMasterProduct.getBasePrice());

        mockAttributeValue = new AttributeValue();
        mockAttributeValue.setId(1L);
        mockAttributeValue.setValue("Color");
        mockAttributeValue.setAttribute(null);  // Set attribute to null or create an Attribute object if needed
    }

    // @Test
    // void testCreateProductSuccess() {
    //     CreateProductDTO createProductDTO = new CreateProductDTO();
    //     createProductDTO.setMasterproductid(1L);
    //     createProductDTO.setName("New Product");
    //     createProductDTO.setStock(10L);
    
    //     CreateProductDTO.AttributeValueDTO attributeDTO = new CreateProductDTO.AttributeValueDTO();
    //     attributeDTO.setId(1L);
    //     attributeDTO.setValue(123L);  // This should be the value ID (1L for example)
    //     createProductDTO.setAttributes(Collections.singletonList(attributeDTO));
    
    //     // Mock MasterProduct
    //     MasterProduct mockMasterProduct = new MasterProduct();
    //     mockMasterProduct.setId(1L);
    //     mockMasterProduct.setName("Master Product");
    
    //     // Mock AttributeValue
    //     AttributeValue mockAttributeValue = new AttributeValue();
    //     mockAttributeValue.setId(1L);
    //     mockAttributeValue.setValue("Color");
    
    //     // Mock Product
    //     Product mockProduct = new Product();
    //     mockProduct.setName("New Product");
    //     mockProduct.setStock(10L);
    
    //     // Mock the necessary repository calls
    //     when(masterProductRepository.findById(1L)).thenReturn(Optional.of(mockMasterProduct));
    //     when(attributeValueRepository.findById(1L)).thenReturn(Optional.of(mockAttributeValue));
    //     when(productRepository.save(any(Product.class))).thenReturn(mockProduct);
    
    //     // Call the service method to create a product
    //     Product createdProduct = productService.createProduct(createProductDTO);
    
    //     // Assert that the product is created successfully
    //     assertNotNull(createdProduct);
    //     assertEquals("New Product", createdProduct.getName());
    //     assertEquals(10L, createdProduct.getStock());
        
    //     // Verify that save method is called
    //     verify(productRepository).save(any(Product.class));
    // }

    // @Test
    // void testCreateProduct_ProductAlreadyExistsException() {
    //     CreateProductDTO createProductDTO = new CreateProductDTO();
    //     createProductDTO.setMasterproductid(1L);
    //     createProductDTO.setName("New Product");
    //     createProductDTO.setStock(10L);
    
    //     MasterProduct masterProduct = new MasterProduct();
    //     masterProduct.setId(1L);
        
    //     Product existingProduct = new Product();
    //     existingProduct.setName("New Product");
    //     existingProduct.setMasterProduct(masterProduct);
    
    //     AttributeValue attributeValue = new AttributeValue();
    //     attributeValue.setId(1L);
    //     attributeValue.setValue("Color");
    
    //     existingProduct.setAttributeValues(Collections.singletonList(attributeValue));
    
    //     when(masterProductRepository.findById(1L)).thenReturn(Optional.of(masterProduct));
    //     when(productRepository.findByMasterProduct(masterProduct)).thenReturn(Optional.of(Collections.singletonList(existingProduct)));
    //     when(attributeValueRepository.findById(1L)).thenReturn(Optional.of(attributeValue));
    
    //     assertThrows(ProductAlreadyExistsException.class, () -> productService.createProduct(createProductDTO));
    
    //     // Verify that the expected methods were called on the mocks
    //     verify(masterProductRepository).findById(1L);
    //     verify(productRepository).findByMasterProduct(masterProduct);
    //     verify(attributeValueRepository).findById(1L);
    // }

    @Test
    void testDeleteProductById() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProductById(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void testGetProducts() {
        List<Product> products = Collections.singletonList(mockProduct);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> fetchedProducts = productService.getProducts();

        assertNotNull(fetchedProducts);
        assertEquals(1, fetchedProducts.size());
        assertEquals("Test Product", fetchedProducts.get(0).getName());
    }

    @Test
    void testGetProductByIdFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        Optional<Product> fetchedProduct = productService.getProductById(1L);

        assertTrue(fetchedProduct.isPresent());
        assertEquals("Test Product", fetchedProduct.get().getName());
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Product> fetchedProduct = productService.getProductById(1L);

        assertFalse(fetchedProduct.isPresent());
    }

    @Test
    void testGetProductsByMasterId() {
        List<Product> products = Collections.singletonList(mockProduct);
        when(masterProductRepository.findById(1L)).thenReturn(Optional.of(mockMasterProduct));
        when(productRepository.findByMasterProduct(mockMasterProduct)).thenReturn(Optional.of(products));

        Optional<List<Product>> fetchedProducts = productService.getProductsByMasterId(1L);

        assertTrue(fetchedProducts.isPresent());
        assertEquals(1, fetchedProducts.get().size());
        assertEquals("Test Product", fetchedProducts.get().get(0).getName());
    }

    @Test
    void testGetStockProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        Optional<Long> stock = productService.getStockProductById(1L);

        assertTrue(stock.isPresent());
        assertEquals(10L, stock.get());
    }

    @Test
    void testPatchStockProductSuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

        Optional<ResponseEntity<Long>> response = productService.patchStockProduct(1L, 5L);

        assertTrue(response.isPresent());
        assertEquals(15L, response.get().getBody());
    }

    // @Test
    // void testPatchStockProductError() {
    //     when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
    //     when(productRepository.save(any(Product.class))).thenThrow(new RuntimeException("Error"));

    //     Optional<ResponseEntity<Long>> response = productService.patchStockProduct(1L, 5L);

    //     assertTrue(response.isPresent());
    //     assertEquals(HttpStatus.BAD_REQUEST, response.get().getStatusCode());
    // }

    @Test
    void testGetProductPriceById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        Double price = productService.getProductPriceById(1L);

        assertEquals(100.0, price);
    }

    @Test
    void testGetProductPriceByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> productService.getProductPriceById(1L));
    }
}
