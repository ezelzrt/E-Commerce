import React, { useState, useEffect } from 'react';
import { Modal, Button, Form, ToggleButtonGroup, ToggleButton, Alert } from 'react-bootstrap';
import { useCart } from '../../layouts/Customer/CustomerLayout';
import {getAttributesByMasterProductId, getMasterProductById} from '../../services/masterproductService';
import { Attribute, AttributeValue, Product, MasterProduct } from '../../common/appTypes';

interface ProductDetailModalProps {
  show: boolean;
  onHide: () => void;
  masterproduct: any;
}

type ProductsAttributesOption = {
  id: number;
  name: string;
  options: { 
    value_id: number;
    value: string;
    productIDs: number[];
  }[];
};


const generateProductsAttributesOptions = (products: Product[]): ProductsAttributesOption[] => {
  const result: ProductsAttributesOption[] = [];

  products.forEach(product => {
    product.attributeValues.forEach(attrValue => {
      const existingAttribute = result.find(attr => attr.id == attrValue.attribute?.id);
      if (!existingAttribute) {
        // Add new attribute
        result.push({
          id: attrValue.attribute?.id ?? 0,
          name: attrValue.attribute?.name ?? "0",
          options: [
            {
              value_id: attrValue.id,
              value: attrValue.value,
              productIDs: [product.id ?? 0],
            }
          ]
        });
      } else {
        // Check if the value already exists for the attribute
        const existingOption = existingAttribute.options.find(option => option.value_id === attrValue.id);
        if (!existingOption) {
          existingAttribute.options.push({
            value_id: attrValue.id,
            value: attrValue.value,
            productIDs: [product.id ?? 0],
          });
        } else {
          // Add the product ID to the existing option
          existingOption.productIDs.push(product.id ?? 0);
        }
      }
    });
  });

  return result;
};


const ProductDetailModal: React.FC<ProductDetailModalProps> = ({ show, onHide, masterproduct }) => {
  const [selectedOptions, setSelectedOptions] = useState<{ [id: number]: string }>({});
  const [errors, setErrors] = useState<{ [id: number]: string }>({});
  const [products, setProducts] = useState<Product[]>([]);
  const [validProductIDs, setValidProductIDs] = useState<any>([]);
  
  const { cartProducts, setCartProducts } = useCart();

  const addProductToCart = (id: number, name: string, unitPrice: number, unitQty: number) => {
    const existingProduct = cartProducts.find(product => product.id === id);
  
    if (existingProduct) {
      setCartProducts(cartProducts.map(product =>
        product.id === id
          ? { ...product, quantity: product.quantity + unitQty }
          : product
      ));
    } else {
      setCartProducts([...cartProducts, {
        id,
        name,
        quantity: unitQty,
        unitPrice,
      }]);
    }
  };


  const handleOptionChange = (id: number, value: string) => {
    const newValidProductIDs = products.filter(product => {
      return product.attributeValues.some(attribute => attribute.attribute?.id === id && attribute.value === value);
    }).map(product => product.id);
    setValidProductIDs(validProductIDs.filter((id: any) => newValidProductIDs.includes(id)))

    setSelectedOptions(prev => ({ ...prev, [id]: value }));
    setErrors(prevErrors => {
      const newErrors = { ...prevErrors };
      delete newErrors[id]; // Borrar el error si se selecciona una opción
      return newErrors;
    });
  };

  const clearSelectedOptions = () => {
    setSelectedOptions({});
    setValidProductIDs(products.map(product => product.id));
    setErrors({});
  };

  const handleConfirm = () => {
    // Verificar si todos los atributos tienen opción seleccionada
    const newErrors: { [id: number]: string } = {};
    productsAttributesOptions.forEach(attribute => {
      if (!selectedOptions[attribute.id]) {
        newErrors[attribute.id] = 'Debe seleccionar una opción para este atributo';
      }
    });

    if (Object.keys(newErrors).length === 0) {

      const matchedProduct = masterproduct?.products.find((product: any) => product.id == validProductIDs[0])

      addProductToCart(matchedProduct.id, matchedProduct.name, masterproduct?.basePrice, 1)
      onHide()
    } else {
      setErrors(newErrors);
    }

  };

  const productsAttributesOptions = generateProductsAttributesOptions(products);
  
  useEffect(() => {
    if (masterproduct && show) {
      const fetchProducts = async () => {
        try {
          const masterProduct = await getMasterProductById(masterproduct.id);
          const products: Product[] = masterProduct.products.map((product: any) =>{
            return {
              id: product.id,
              masterproduct: masterproduct.id,
              name: product.name,
              stock: product.availableStock,
              imageURL: product.imageURL,
              attributeValues: product.attributeValues.map((attributeValue: any) => {
                return {
                  id: attributeValue.id,
                  value: attributeValue.value,
                  attribute: {
                    id: attributeValue.attribute.id,
                    name: attributeValue.attribute.name,
                    description: attributeValue.attribute.name
                  }
                }
              })
            }
          });
          setProducts(products);
          setSelectedOptions({});
          setValidProductIDs(products.map(product => product.id));
        } catch (error) {
            console.error('Error fetching products:', error);
        }
      };
      fetchProducts();
  }
  }, [masterproduct]);
  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>Product details</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <h4>{masterproduct?.name}</h4>
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%' }}>
          <img src="images/items/box.svg" alt={masterproduct?.name} style={{ width: '70%' }} />
        </div>
        <p>{masterproduct?.description}</p>
        {productsAttributesOptions.map((attribute) => (
          <Form.Group key={attribute.id} className="mb-3">
            <Form.Label className="fw-bold">{attribute.name}</Form.Label>
            <br />
            <ToggleButtonGroup
              type="radio"
              name={`attributes-${attribute.id}`}
              value={selectedOptions[attribute.id] || ''}
              onChange={(value) => handleOptionChange(attribute.id, value as string)}
            >
              {attribute.options.map((option) => (
                <ToggleButton
                  variant="dark"
                  key={option.value}
                  id={`tbg-radio-${option.value}`}
                  value={option.value}
                  className="mb-2 custom-toggle-button"
                  disabled={!validProductIDs.some((id: any) => option.productIDs.includes(id))}
                >
                  {option.value}
                </ToggleButton>
              ))}
            </ToggleButtonGroup>
            {errors[attribute.id] && (
              <Alert variant="danger" className="mt-2">{errors[attribute.id]}</Alert>
            )}
          </Form.Group>
        ))}
        {Object.keys(selectedOptions).length > 0 && (
          <Button variant="outline-secondary" onClick={clearSelectedOptions}>
            Clear selection
          </Button>
        )}
      </Modal.Body>
      <Modal.Footer className="d-flex w-100">
        <label className="text-start">${masterproduct?.basePrice.toFixed(2)}</label>
        <div className="ms-auto">
          <Button variant="secondary" className='me-2' onClick={onHide}>
            Close
          </Button>
          <Button variant="primary" onClick={handleConfirm}>
            Add to cart
          </Button>
        </div>
      </Modal.Footer>
    </Modal>
  );
};

export default ProductDetailModal;
