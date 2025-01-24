import React, {useState, createContext, useContext, useEffect} from 'react';
import './customerStyle.css';
import { useAuth } from '../../contexts/AuthContext';
import CustomerNavbar from '../../components/CustomerNavbar';
import { Spinner } from 'react-bootstrap';
import { CartProduct } from '../../common/appTypes';
import { getCartProducts } from '../../services/cartService';

type CartContextType = {
  cartProducts: any[];
  setCartProducts: React.Dispatch<React.SetStateAction<any[]>>;
};

const CartContext = createContext<CartContextType | undefined>(undefined);

export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
};

const CustomerLayout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated, isLoading } = useAuth();
  const [cartProducts, setCartProducts] = useState<CartProduct[]>([]);

  // Esto cargaría un carrito persistido en la db o en sesion

   // Cargar el carrito desde sessionStorage cuando el componente se monta
   useEffect(() => {
    // const fetchCartProducts = async () => {
    
    //   try {
    //     const products = await getCartProducts();
    //     setCartProducts(products);
    //   } catch (err) {
    //     console.error(err);
    //   }
    // };
    // fetchCartProducts();

    const savedCart = sessionStorage.getItem('cartProducts');
    if (savedCart) {
      setCartProducts(JSON.parse(savedCart)); 
    } else {
      setCartProducts([ ]);
      // setCartProducts([ { productID: 1, name: 'Producto A', quantity: 2, unitPrice: 15.99 },
      //   { productID: 2, name: 'Producto B', quantity: 1, unitPrice: 25.5 },
      //   { productID: 3, name: 'Producto C', quantity: 3, unitPrice: 9.99 },]);
    }
  }, [setCartProducts]);

  // Guardar cartProducts en sessionStorage cada vez que cambie
  useEffect(() => {
    if (cartProducts.length > 0) {
      sessionStorage.setItem('cartProducts', JSON.stringify(cartProducts));
    }
  }, [cartProducts]);

  if (isLoading) {
    return (
      <Spinner animation="border" role="status">
        <span className="visually-hidden">Loading...</span>
      </Spinner>
    );
  }

  return (
    <CartContext.Provider value={{ cartProducts, setCartProducts }}>
      <div className="customer-layout">
        <CustomerNavbar />
        <div className="customer-container">{children}</div>
    </div>
    </CartContext.Provider>
  );
};

export default CustomerLayout;
