import React, { useEffect, useState } from 'react';
import ProductList from '../../components/secure/products/ProductList';

const SecureProduct: React.FC = () => {
    return (
      <>
        <h1>Products</h1>
        <ProductList/>
      </>
    );
  };
  
  export default SecureProduct;
  