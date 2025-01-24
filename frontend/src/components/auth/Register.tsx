import React, { useState, useEffect } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { register } from '../../services/authService';
import Form from 'react-bootstrap/Form';
import { Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom'; 
import { jwtDecode } from 'jwt-decode';

const Register: React.FC = () => {
  const navigate = useNavigate(); 
  const { login, logout } = useAuth();
  const [user, setUser] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    age: 0,
    gender: '',
    address: '',
    photo: '123.png'
  });

  useEffect(() => {
    logout();
  }, [logout]);


  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await register(user);
      if (response && response["access_token"]) {
        const decodedToken: any = jwtDecode(response["access_token"]);
        const accessType = decodedToken.accessType;
        const name = decodedToken.name;
  
        login(response["access_token"], response["refresh_token"], name, accessType); 

        navigate('/home');
      } else {
        throw new Error('Error during register');
      }
    } catch (err) {
      console.error(err);
    }
  };

  const handleLoginRedirect = () => {
    navigate('/login'); 
  };
  return (
    <>
      <h2 className="text-center">Register</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>First name</Form.Label>
          <Form.Control
              type="text"
              placeholder="First Name"
              value={user.firstName}
              onChange={(e) => setUser({ ...user, firstName: e.target.value })}
              required
          />

          <Form.Label>Last name</Form.Label>
          <Form.Control
              type="text"
              placeholder="Last Name"
              value={user.lastName}
              onChange={(e) => setUser({ ...user, lastName: e.target.value })}
              required
          />

          <Form.Label>Email</Form.Label>
          <Form.Control
              type="email"
              placeholder="Email"
              value={user.email}
              onChange={(e) => setUser({ ...user, email: e.target.value })}
              required
          />
            
          <Form.Label>Password</Form.Label>
          <Form.Control
            type="password"
            placeholder="Password"
            value={user.password}
            onChange={(e) => setUser({ ...user, password: e.target.value })}
            required
          />

          <Form.Label>Age</Form.Label>
          <Form.Control
            type="number"
            placeholder="Age"
            value={user.age}
            onChange={(e) => setUser({ ...user, age: parseInt(e.target.value) })}
            required
          />

          <Form.Label>Gender</Form.Label>
          <Form.Control
            type="text"
            placeholder="Gender"
            value={user.gender}
            onChange={(e) => setUser({ ...user, gender: e.target.value })}
          />

          <Form.Label>Address</Form.Label>
          <Form.Control
            type="text"
            placeholder="Address"
            value={user.address}
            onChange={(e) => setUser({ ...user, address: e.target.value })}
            required
          />
        </Form.Group>
        <Button type="submit"
                className="w-100"
        >
          Register
        </Button>
        <hr />
        <Button 
          type="button"
          className="w-100 btn-secondary"
          onClick={handleLoginRedirect}
        >
          Login
        </Button>
      </Form>
    </>
  );
};

export default Register;
