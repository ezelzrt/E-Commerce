import React, { useState, useEffect } from 'react';
import { Button, Form, Alert, Spinner } from 'react-bootstrap';
import { createProduct } from '../../../services/productService';
import { getMasterProducts, getAttributesByMasterProductId } from '../../../services/masterproductService';
import AsyncSelect from 'react-select/async';
import Select from 'react-select';
import { MasterProduct, Product } from '../../../common/appTypes';

interface CreateProductProps {
    onCreateProduct: (product: Product) => void;
}

const CreateProduct: React.FC<CreateProductProps> = ({ onCreateProduct }) => {
    const [name, setName] = useState<string>('');
    const [stock, setStock] = useState<number>(0);
    const [selectedMasterProduct, setSelectedMasterProduct] = useState<MasterProduct | null>(null);
    const [masterProducts, setMasterProducts] = useState<MasterProduct[]>([]);
    const [loading, setLoading] = useState<boolean>(true); 
    const [attributes, setAttributes] = useState<any[]>([]);
    const [selectedAttributes, setSelectedAttributes] = useState<{ [key: number]: any }>({});
    const [alertMessage, setAlertMessage] = useState<string | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!selectedMasterProduct) {
            setAlertMessage(`No master product selected`);
            return;
        }

        const missingAttributes = attributes.filter(attribute => {
            return selectedAttributes[attribute.id] == null;
        });

    
        if (missingAttributes.length > 0) {
            const missingNames = missingAttributes.map(attribute => attribute.name).join(', ');
            setAlertMessage(`The following attributes are missing for selection: ${missingNames}`);
            return; 
        }

        try {
            setAlertMessage(null);
            const newProduct = await createProduct({
                masterproductid: selectedMasterProduct.id,
                name,
                stock,
                attributes: Object.entries(selectedAttributes).map(([id, value]) => ({
                    id: Number(id),
                    value
                }))
            });
            onCreateProduct(newProduct);
        } catch (error: any) {
            setAlertMessage(error?.response?.data?.message);
        }
    };

    const handleAttributeChange = (attributeId: number, selectedOption: any) => {
        setSelectedAttributes(prevState => ({
            ...prevState,
            [attributeId]: selectedOption ? selectedOption.value : null
        }));
    };

    useEffect(() => {
        const fetchMasterProducts = async () => {
            try {
                const products = await getMasterProducts();
                setMasterProducts(products);
            } catch (error) {
                console.error('Error fetching master products:', error);
            } finally {
                setLoading(false);
            }
        };
        fetchMasterProducts();
    }, []);

    useEffect(() => {
        if (selectedMasterProduct) {
            const fetchAttributes = async () => {
                try {
                    const attributes = await getAttributesByMasterProductId(selectedMasterProduct.id);
                    setAttributes(attributes);
                } catch (error) {
                    console.error('Error fetching attributes:', error);
                }
            };
            fetchAttributes();
        }
    }, [selectedMasterProduct]);

    const loadOptions = (inputValue: string, callback: (options: any[]) => void) => {
        const filteredProducts = masterProducts.filter(product =>
            product.name.toLowerCase().includes(inputValue.toLowerCase())
        );
        callback(filteredProducts.map(product => ({
            label: product.name,
            value: product.id,
        })));
    };

    if (loading) {
        return <Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
        </Spinner>;
    }

    return (
        <Form onSubmit={handleSubmit}>
            {alertMessage && (
                <Alert variant="warning" onClose={() => setAlertMessage(null)} dismissible>
                    {alertMessage}
                </Alert>
            )}
            <Form.Group controlId="productMasterProduct" className="mb-3">
                <Form.Label>Master Product</Form.Label>
                <AsyncSelect
                    cacheOptions
                    loadOptions={loadOptions}
                    defaultOptions
                    onChange={(selectedOption) =>
                    {
                        const auxSelectedMasterproduct = masterProducts.find(
                                (product) => product.id === selectedOption?.value
                            ) || null 
                        setName(auxSelectedMasterproduct?.name ?? "");
                        return   setSelectedMasterProduct(auxSelectedMasterproduct)
                    }
                    }
                    getOptionLabel={(e) => e.label}
                    getOptionValue={(e) => e.value.toString()}
                />
            </Form.Group>
            <Form.Group controlId="productName" className="mb-3">
                <Form.Label>Product Name</Form.Label>
                <Form.Control
                    type="text"
                    placeholder="Enter product name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                />
            </Form.Group>
            <Form.Group controlId="productStock" className="mb-3">
                <Form.Label>Stock</Form.Label>
                <Form.Control
                    type="number"
                    placeholder="Enter stock quantity"
                    value={stock}
                    onChange={(e) => setStock(Number(e.target.value))}
                    required
                />
            </Form.Group>

            {attributes.length > 0 && (
                <h4 className="mb-3" style={{ color: '#333', fontWeight: 'bold' }}>
                    Attributes
                </h4>
            )}

            {/* Aquí se generan los selectores dinámicos */}
            {attributes.map((attribute) => (
                <Form.Group key={attribute.id} controlId={`productAttribute-${attribute.id}`} className="mb-3">
                    <Form.Label>{attribute.name}</Form.Label>
                    <Select
                        options={attribute.values.map((value: { id: number, value: string }) => ({
                            label: value.value,
                            value: value.id
                        }))}
                        onChange={(selectedOption) => handleAttributeChange(attribute.id, selectedOption)}
                    />
                </Form.Group>
            ))}

            <Button variant="primary" type="submit">
                Create Product
            </Button>
        </Form>
    );
};

export default CreateProduct;
