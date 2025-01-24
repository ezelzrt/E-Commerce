// src/pages/Product.tsx

import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Button, Offcanvas } from 'react-bootstrap';
import ProductCard from '../components/ProductPresentation/ProductCarrouselCard';
import { Link } from 'react-router-dom';
import ProductDetailModal from '../components/ProductPresentation/ProductDetailModal';  // Importamos el nuevo componente
import { MasterProduct } from '../common/appTypes';
import { apiRequestHandler } from '../utils/apiRequestHandler';
// import { getProducts } from '../services/productService';
import { getMasterProducts } from '../services/masterproductService';
import LoadingSpinner from '../common/loadingSpinner';

const ProductPage: React.FC = () => {
  const [showFilters, setShowFilters] = useState(false);
  const [showModal, setShowModal] = useState(false); // Estado para controlar el modal
  const [selectedMasterProduct, setSelectedMasterProduct] = useState<any>(null); // Estado para el producto seleccionado
  const [masterproducts, setMasterproducts] = useState<MasterProduct[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  const toggleFilters = () => setShowFilters(!showFilters);

  // Abre el modal y establece el producto seleccionado
  const handleShowModal = (masterproduct: any) => {
    setSelectedMasterProduct(masterproduct);
    setShowModal(true);
  };

  const handleCloseModal = () => setShowModal(false);

  const filterNoProducts = (masterproducts: MasterProduct[]) => {
    return masterproducts.filter(mp => mp.products.length != 0);
  } 

  useEffect(() => {
      setIsLoading(true);
      apiRequestHandler(getMasterProducts, setMasterproducts, filterNoProducts)
          .finally(() => setIsLoading(false));
  }, []);

  return (
    <>
      <h1>Products</h1>
      <Container fluid>
        <Row>
          <Col xs={12} md={3} lg={2} className="d-none d-md-block">
            <div className="p-3 bg-light">
              <h5>Filters</h5>
            </div>
          </Col>
          <Col xs={12} md={9} lg={10}>
            <div className="p-3">
              <Button variant="primary" onClick={toggleFilters} className="d-md-none mb-3">
                Show filters
              </Button>
              <Row>
                { isLoading ? (
                  <LoadingSpinner />
                ) : masterproducts.length === 0 ? (
                    <p>No products available</p>
                ) : (
                  masterproducts.map((masterproduct, index) => (
                  <Col key={index} xs={12} sm={6} md={4} lg={3} className="mb-4">
                    <Link
                      to="#"
                      style={{ textDecoration: 'none' }}
                      onClick={() => handleShowModal(masterproduct)} // Llamar al método para abrir el modal
                    >
                      <ProductCard
                        imageUrl="/images/items/box.svg"
                        title={masterproduct.name}
                        description={masterproduct.description}
                        price={masterproduct.basePrice}
                      />
                    </Link>
                  </Col>
                )))}
              </Row>
            </div>
          </Col>
        </Row>

        <Offcanvas show={showFilters} onHide={toggleFilters} placement="start">
          <Offcanvas.Header closeButton>
            <Offcanvas.Title>Filtros</Offcanvas.Title>
          </Offcanvas.Header>
          <Offcanvas.Body>
            {/* Aquí se pueden agregar filtros */}
          </Offcanvas.Body>
        </Offcanvas>

        {/* Usamos el componente ProductDetailModal */}
        <ProductDetailModal 
            show={showModal} 
            onHide={handleCloseModal} 
            masterproduct={selectedMasterProduct} 
          />
        </Container>
    </>
  );
};

export default ProductPage;
