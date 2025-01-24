import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Carousel, Card } from 'react-bootstrap'; 
import ProductCard from '../components/ProductPresentation/ProductCarrouselCard';
import { MasterProduct } from '../common/appTypes';
import { apiRequestHandler } from '../utils/apiRequestHandler';
import { getMasterProducts } from '../services/masterproductService';
import ProductDetailModal from '../components/ProductPresentation/ProductDetailModal';


const chunkArray = (arr: any[], chunkSize: number) => {
  const result: any[] = [];
  for (let i = 0; i < arr.length; i += chunkSize) {
    result.push(arr.slice(i, i + chunkSize));
  }
  return result;
};

const filterNoProducts = (masterproducts: MasterProduct[]) => {
  return masterproducts.filter(mp => mp.products.length != 0);
} 

const HomePage: React.FC = () => {
  const [masterproducts, setMasterProducts] = useState<MasterProduct[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [showModal, setShowModal] = useState(false); // Estado para controlar el modal
  const [selectedMasterProduct, setSelectedMasterProduct] = useState<any>(null); // Estado para el producto seleccionado

  useEffect(() => {
    setIsLoading(true);
    apiRequestHandler(getMasterProducts, setMasterProducts, filterNoProducts)
        .finally(() => setIsLoading(false));
  }, []);
  const productChunks = chunkArray(masterproducts.slice(0,12), 4);

  const handleShowModal = (masterproduct: any) => {
    setSelectedMasterProduct(masterproduct);
    setShowModal(true);
  };

  const handleCloseModal = () => setShowModal(false);

  return (
    <div>
      <Container className="text-center" style={{ height: '50vh', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
        <h1>NOMBRE DE LA MARCA</h1>
      </Container>

      <hr style={{ borderColor: '#ddd', margin: '20px 0' }} />

      <Container>
        <Row>
          <Col>
            <Carousel indicators={false} variant='dark'  controls={true} interval={null}>
              {productChunks.map((chunk, index) => (
                <Carousel.Item key={index}>
                  <Row className='justify-content-center align-items-center'>
                    {chunk.map((masterproduct:MasterProduct, index2:number) => (
                      <Col xl={2} sm={5} xs={8} key={index2} onClick={() => handleShowModal(masterproduct)} >
                        <ProductCard 
                          imageUrl="/images/items/box.svg" 
                          title={masterproduct.name} 
                          description={masterproduct.description}
                          price={masterproduct.basePrice}
                        />
                      </Col>
                    ))}
                  </Row>
                </Carousel.Item>
              ))}
            </Carousel>
          </Col>
        </Row>
      </Container>
      <ProductDetailModal 
            show={showModal} 
            onHide={handleCloseModal} 
            masterproduct={selectedMasterProduct} 
          />
    </div>
  );
};

export default HomePage;
