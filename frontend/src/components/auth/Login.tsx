import React, { useState, useEffect } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { login } from '../../services/authService';
import Form from 'react-bootstrap/Form';
import { Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom'; 
import { jwtDecode } from 'jwt-decode';

const Login: React.FC = () => {
  const { isAuthenticated, login: authLogin, logout } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate(); 

  useEffect(() => {
    logout();
  }, [logout]);


  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await login({ email, password });
      if (response && response["access_token"]) {
        const decodedToken: any = jwtDecode(response["access_token"]);
        const accessType = decodedToken.accessType;
        const name = decodedToken.name;
  
        authLogin(response["access_token"], response["refresh_token"], name, accessType); 

        if (accessType === 1) {
          navigate('/secure/products'); 
        } else if (accessType === 2) {
          const savedCart = JSON.parse(sessionStorage.getItem('cartProducts') ?? '{}');
          console.log(savedCart)
          if(savedCart.length > 0)
            navigate('/cart');
          else
            navigate('/home');
        }
      } else {
        throw new Error('Token not found in response');
      }
    } catch (err) {
      setError('Invalid email or password');
    }
  };

  const handleRegisterRedirect = () => {
    navigate('/register'); 
  };

  return (
    <>
      <h2 className="text-center">Login</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Email</Form.Label>
            <Form.Control
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            <Form.Label>Password</Form.Label>
            <Form.Control
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
        </Form.Group>
        <Button type="submit"
                className="w-100"
        >
          Login
        </Button>
        {error && <p className="text-danger">{error}</p>}
        <Form.Text className="d-flex justify-content-center w-100">
          <a href="/forgot-password">Forgot your password?</a>
        </Form.Text>
        <hr />
        <Button 
          type="button" 
          className="w-100 btn-secondary"
          onClick={handleRegisterRedirect}
        >
          Register new account
        </Button>
      </Form>
    </>
  );
};

export default Login;
