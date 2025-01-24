import React, { useEffect, useState } from 'react';
import { Col, InputGroup, Modal, Row, Table } from 'react-bootstrap';
import { getProducts } from '../../../services/productService'; 
import Alert from 'react-bootstrap/Alert';
import Button from 'react-bootstrap/Button';
import CreateProduct from './CreateProduct';
import { Product } from '../../../common/appTypes';
import Form from 'react-bootstrap/Form';

const printProductName = (product: Product): String => {
    if(!product.name || product.name?.toLowerCase() == "null")
        return product.masterproduct.name;

    return product.name

}

const ProductList: React.FC = () => {
    const [products, setProducts] = useState<Product[]>([]);
    const [error, setError] = useState<string>('');
    const [showCreateProduct, setshowCreateProduct] = useState(false);

    useEffect(() => {
        const fetchData = async () => {
        try {
            const fetchedProducts = await getProducts();
            setProducts(fetchedProducts); 
        } catch (err) {
            setError('Failed to load products');
            console.error(err);
        }
        };

        fetchData();
    }, []);

    const handleCreateProduct  = (newProduct: Product) => {
        setProducts([...products, newProduct]);
        setshowCreateProduct(false);  
    };

    return (
        <>
        <div className="d-flex justify-content-between mb-2">
            <div>
            </div>
            <Button variant="outline-primary" onClick={() => setshowCreateProduct(true)}>
                Add product
            </Button>
        </div>
        {products.length === 0 ? (
            <Alert variant={'warning'}>
                No products found
            </Alert>
        ) : (
            <Table striped>
                <thead>
                <tr>
                    <th>Masterproduct</th>
                    <th>#</th>
                    <th>Name</th>
                    <th>Attributes</th>
                    <th className='text-end'>Stock</th>
                </tr>
                </thead>
                <tbody>
                {products.map((product) => (
                    <tr key={product.id}>
                    <td>[{product.masterproduct.id}] - {product.masterproduct.name}</td>
                    <td>{product.id}</td>
                    <td>{printProductName(product)}</td>
                    <td>
                        <Row>
                            {product.attributeValues.map(attr => (
                                <Col xs={2} key={`${product.id}-${attr.id}`}>
                                    <InputGroup className="mb-3"  style={{ width: 'auto' }}>
                                        <InputGroup.Text id="basic-addon1">{attr.name}</InputGroup.Text>
                                        <Form.Control disabled value={attr.value}/>
                                    </InputGroup>
                                </Col>
                            ))}
                        </Row>
                    </td>
                    <td className='text-end'>{product.stock}</td>
                    </tr>
                ))}
                </tbody>
            </Table>
        )}
        <Modal show={showCreateProduct} onHide={() => setshowCreateProduct(false)}>
            <Modal.Header closeButton>
                <Modal.Title>Add Product</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <CreateProduct onCreateProduct={handleCreateProduct} />
            </Modal.Body>
        </Modal>
        </>
    );
};

export default ProductList;
