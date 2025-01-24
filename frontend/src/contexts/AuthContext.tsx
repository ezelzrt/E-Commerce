import React, { createContext, useState, useContext, useEffect } from 'react';
import { jwtDecode } from "jwt-decode";

interface AuthContextType {
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (access_token: string, refresh_token: string, username: string, accessType: number) => void;
  logout: () => void;
  username: string | null;
  accessType: number | null;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [username, setUsername] = useState<string | null>(null);
  const [accessType, setAccessType] = useState<number | null>(null); 
  const [isLoading, setIsLoading] = useState<boolean>(true);

  useEffect(() => {
    const access_token = sessionStorage.getItem('access_token');
    const refresh_token = sessionStorage.getItem('refresh_token');
    const storedUsername = sessionStorage.getItem('username');
    const storedAccessType = sessionStorage.getItem('accessType');
    
    setIsAuthenticated(!!access_token);
    setUsername(storedUsername);
    setAccessType(storedAccessType ? parseInt(storedAccessType, 10) : null);
    setIsLoading(false); 
  }, []);

  const login = (access_token: string, refresh_token: string, username: string, accessType: number) => {
    const decoded = jwtDecode(access_token);

    sessionStorage.setItem('access_token', access_token);
    sessionStorage.setItem('refresh_token', refresh_token);
    sessionStorage.setItem('username', username);
    sessionStorage.setItem('accessType', accessType.toString());
    
    setIsAuthenticated(true);
    setUsername(username);
    setAccessType(accessType); 
  };

  const logout = () => {
    sessionStorage.removeItem('access_token');
    sessionStorage.removeItem('refresh_token');
    sessionStorage.removeItem('username');
    sessionStorage.removeItem('accessType');
    setIsAuthenticated(false);
    setUsername(null);
    setAccessType(null); 
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, isLoading, login, logout, username, accessType }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  
  return context;
};
