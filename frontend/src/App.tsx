import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import HomePage from './pages/HomePage';
import Login from './components/auth/Login';
import Register from './components/auth/Register';
import Logout from './components/auth/Logout';
import AuthLayout from './layouts/Auth/AuthLayout';
import MasterProducts from './pages/secure/MasterProducts';
import SecureProduct from './pages/secure/SecureProduct';
import SecureLayout from './layouts/Secure/SecureLayout';
import Attributes from './pages/secure/Attributes';
import CustomerLayout from './layouts/Customer/CustomerLayout';
import Cart from './pages/Cart';
import Product from './pages/ProductPage';
import OrderPage from './pages/OrderPage'; 
import OrderDetailsPage from './pages/OrderDetailsPage';
import NotFound from './pages/NotFound';
import Rules from './pages/secure/Rules';

const App: React.FC = () => {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/" 
                element={<AuthLayout>
                            <Login />
                          </AuthLayout>
                        } 
          />
          <Route path="/login" 
                element={<AuthLayout>
                            <Login />
                          </AuthLayout>
                        } 
          />
          <Route path="/register" 
                element={<AuthLayout>
                            <Register />
                          </AuthLayout>
                        } 
          />
          <Route path="/logout" 
                element={
                          <Logout />
                        } 
          />
          <Route path="/secure/masterproducts" element={<SecureLayout><MasterProducts /></SecureLayout>} />
          <Route path="/secure/products" element={<SecureLayout><SecureProduct /></SecureLayout>} />
          <Route path="/secure/attributes" element={<SecureLayout><Attributes /></SecureLayout>} />
          <Route path="/secure/orders" element={<SecureLayout><OrderPage /></SecureLayout>} />
          <Route path="/secure/orders/:orderID" element={<SecureLayout><OrderDetailsPage /></SecureLayout>} />
          <Route path="/secure/rules" element={<SecureLayout><Rules /></SecureLayout>} />

          <Route path="/home" 
                element={<CustomerLayout>
                            <HomePage />
                          </CustomerLayout>
                        } 
          />
          <Route path="/products" 
                element={<CustomerLayout>
                            <Product />
                          </CustomerLayout>
                        } 
          />
          <Route path="/orders"
                element={<CustomerLayout>
                            <OrderPage />
                          </CustomerLayout>
                        }
          />
          <Route path="/cart" 
                element={<CustomerLayout><Cart /></CustomerLayout>} 
          /> 

          <Route path="/orders/:orderID" 
                element={<CustomerLayout><OrderDetailsPage /></CustomerLayout>} 
          /> 

          <Route path="*" element={<NotFound />} />
        </Routes>
      </Router>
    </AuthProvider>
    
  );
};

export default App;