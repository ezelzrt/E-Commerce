import React, { useEffect } from 'react';
import { Navigate } from 'react-router-dom';
import './secureStyle.css';
import SecureNavbar from '../../components/secure/SecureNavbar';
import { useAuth } from '../../contexts/AuthContext';
import { Spinner } from 'react-bootstrap';

const SecureLayout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated, isLoading, accessType } = useAuth();

  if (isLoading) {
    return (
      <Spinner animation="border" role="status">
        <span className="visually-hidden">Loading...</span>
      </Spinner>
    );
  }

  if (!isAuthenticated || accessType !== 1) {
    return <Navigate to="/login" />;
  }

  return (
    <div className="secure-layout">
      <SecureNavbar />
      <div className="secure-container">{children}</div>
    </div>
  );
};

export default SecureLayout;
