import React from 'react';
import { Col, Card } from 'react-bootstrap';

interface ProductCardProps {
  imageUrl: string;
  title: string;
  description: string;
  price: number;
}

const ProductCard: React.FC<ProductCardProps> = ({ imageUrl, title, description, price }) => {
  return (
    <Card style={{ maxWidth: '300px', maxHeight: '400px', overflow: 'hidden' }} className="d-flex flex-column">
      <Card.Img variant="top" src={imageUrl} style={{ maxHeight: '200px', objectFit: 'cover' }} />
      <Card.Body className="d-flex flex-column">
        <Card.Title style={{ overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{title}</Card.Title>
        <Card.Text style={{ overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{description}</Card.Text>
        <Card.Text style={{ overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>${price.toFixed(2)}</Card.Text>
      </Card.Body>
    </Card>
  );
};

export default ProductCard;
