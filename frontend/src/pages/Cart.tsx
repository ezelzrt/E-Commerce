import React, { useState } from 'react';
import { Table, Button, Alert, InputGroup, FormControl } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { confirmPurchase, } from '../services/cartService';
import { useCart } from '../layouts/Customer/CustomerLayout';

const Cart: React.FC = () => {
  const { cartProducts, setCartProducts } = useCart();
  const [error, setError] = useState<string>('');
  const [purchaseMessage, setPurchaseMessage] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleConfirmPurchase = async () => {
    try {
      const message = await confirmPurchase(cartProducts);
      setPurchaseMessage(message); 

      setCartProducts([]); 
      sessionStorage.setItem('cartProducts',JSON.stringify([]));
    } catch (error: any) {
        setPurchaseMessage(error.message || 'An error occurred during purchase');
    }
  };

  const handleContinueShopping = () => {
    navigate('/home');
  };

  const handleDelete = (index: number) => {
    // Eliminar el producto por índice
    const updatedCart = cartProducts.filter((_, i) => i !== index);
    setCartProducts(updatedCart);
    sessionStorage.setItem('cartProducts', JSON.stringify(cartProducts));
  };

  const handleIncrement = (index: number) => {
    const updatedCart = [...cartProducts];
    updatedCart[index].quantity += 1;
    setCartProducts(updatedCart);
  };

  const handleDecrement = (index: number) => {
    const updatedCart = [...cartProducts];
    if (updatedCart[index].quantity > 1) {
      updatedCart[index].quantity -= 1;
      setCartProducts(updatedCart);
    }
  };

  const handleChange = (e: React.ChangeEvent<any>, index: number) => {
    let newQuantity = parseInt(e.target.value, 10);
    if(e.target.value == '') newQuantity = 1;
    if (!isNaN(newQuantity) && newQuantity >= 1) {
      const updatedCart = [...cartProducts];
      updatedCart[index].quantity = newQuantity;
      setCartProducts(updatedCart);
    }
  };
  

  return (
    <div>
      <h1>Cart</h1>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {purchaseMessage && (
        <Alert variant={purchaseMessage === 'Order created successfully!' ? 'success' : 'danger'}>
          {purchaseMessage}
        </Alert>
      )}
      {cartProducts.length > 0 ? 
        (
        <>
        <Table striped>
          <thead>
            <tr>
              <th>Name</th>
              <th className="text-center">Quantity</th>
              <th className="text-end">Unit Price</th>
              <th className="text-end">Total</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {cartProducts.map((product, index) => (
              <tr key={index} className="align-middle">
                <td>{product.name}</td>
                <td className="text-center">
                  <InputGroup className='w-25 mx-auto'>
                    <Button variant="outline-secondary" onClick={() => handleDecrement(index)}>
                      -
                    </Button>
                    <FormControl
                      type="number"
                      value={product.quantity}
                      onChange={(e) => handleChange(e, index)}
                      className="text-center"
                    />
                    <Button variant="outline-secondary" onClick={() => handleIncrement(index)}>
                      +
                    </Button>
                  </InputGroup>
                </td>
                <td className="text-end">${product.unitPrice.toFixed(2)}</td>
                <td className="text-end">${(product.quantity * product.unitPrice).toFixed(2)}</td>
                <td>
                  <Button variant="outline-danger" onClick={() => handleDelete(index)}>
                    <i className="bi bi-trash-fill" />
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
          <tfoot>
            <tr>
              <th>Total</th>
              <td className="text-center">{cartProducts.reduce((acum,e) => acum + e.quantity, 0)}</td>
              <td className="text-end">${cartProducts.reduce((acum,e) => acum + e.unitPrice, 0).toFixed(2)}</td>
              <td className="text-end">${cartProducts.reduce((acum,e) => acum + e.unitPrice * e.quantity, 0).toFixed(2)}</td>
              <td></td>
            </tr>
          </tfoot>
        </Table>

        <div style={{ marginTop: '20px' }}>
          <Button variant="secondary" onClick={handleContinueShopping}>
            Keep shopping
          </Button>
          <Button variant="primary" onClick={handleConfirmPurchase} className="ms-2">
            Confirm
          </Button>
        </div>
        </>
        )
        :
        (<Alert variant="info">
          Your cart is empty.{' '}
          <span
            style={{ color: 'blue', cursor: 'pointer' }}
            onClick={() => navigate('/products')}
          >
            Click here
          </span>{' '} to start shopping and fill it up!
        </Alert>)
      }
    </div>
  );
};

export default Cart;
