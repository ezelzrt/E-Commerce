import { useState, useEffect } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import Button from 'react-bootstrap/Button';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import Offcanvas from 'react-bootstrap/Offcanvas';
import { useNavigate } from 'react-router-dom';

function SecureNavbar() {
  const [expand, setExpand] = useState<string>("sm");
  const { username, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    console.log("Logout successful");
  };

  useEffect(() => {
    const handleResize = () => {
      setExpand(window.innerWidth < 800 ? "false" : "sm");
    };
    window.addEventListener('resize', handleResize);
    handleResize();
    return () => window.removeEventListener('resize', handleResize);
  }, []);
  
  return (
    <Navbar expand={expand} className="bg-body-tertiary mb-3">
      <Container fluid>
        <Navbar.Brand href="#">GRUPO 13 - Administration</Navbar.Brand>
        <Navbar.Toggle aria-controls={`offcanvasNavbar`} />
        <Navbar.Offcanvas
          id={`offcanvasNavbar`}
          aria-labelledby={`offcanvasNavbarLabel`}
          placement="end"
        >
          <Offcanvas.Header closeButton>
            <Offcanvas.Title id={`offcanvasNavbarLabel`}>
              Offcanvas
            </Offcanvas.Title>
          </Offcanvas.Header>
          <Offcanvas.Body>
            <Nav className="justify-content-end flex-grow-1 pe-3">
              <NavDropdown title="SetUp" id={`offcanvasNavbarSetUp`}>
                <NavDropdown.Item onClick={() => navigate('/secure/masterproducts', { replace: true })}>Masterproducts</NavDropdown.Item>
                <NavDropdown.Item onClick={() => navigate('/secure/attributes', {replace: true})}>Attributes</NavDropdown.Item>
                {/* Agregar el enlace de Rules */}
                <NavDropdown.Item onClick={() => navigate('/secure/rules', { replace: true })}>Rules</NavDropdown.Item>
              </NavDropdown>
            </Nav>
            <Nav className="justify-content-end flex-grow-1 pe-3">
              <NavDropdown title="Inventory" id={`offcanvasNavbarInventory`}>
                <NavDropdown.Item onClick={() => navigate('/secure/products', {replace: true})}>Products</NavDropdown.Item>
              </NavDropdown>
            </Nav>
            <Nav className="justify-content-end flex-grow-1 pe-3">
              <NavDropdown title="Commercial" id={`offcanvasNavbarCommercial`}>
                <NavDropdown.Item onClick={() => navigate('/secure/orders', {replace: true})}>Orders</NavDropdown.Item>
              </NavDropdown>
            </Nav>
            <Nav className="justify-content-end flex-grow-1 pe-3">
              <span className="navbar-text me-3">{username}</span>
              <Button variant="outline-danger" onClick={handleLogout}>Logout</Button>
              <div className='m-1'></div>
              <Button className="" variant="success" onClick={()=>navigate('/home',{ replace: true })}>Change to customer view</Button>
            </Nav>
          </Offcanvas.Body>
        </Navbar.Offcanvas>
      </Container>
    </Navbar>
  );
}

export default SecureNavbar;
