import { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import Button from 'react-bootstrap/Button';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import Offcanvas from 'react-bootstrap/Offcanvas';
import { useLocation } from 'react-router-dom'; // Para navegación react-router
import { useNavigate } from 'react-router-dom';
import { Badge, Modal } from 'react-bootstrap';
import { useCart } from '../layouts/Customer/CustomerLayout';

function CustomerNavbar() {
  const { cartProducts } = useCart();
  
  const [expand, setExpand] = useState<string>("sm");
  const { username, logout, isAuthenticated, accessType}  = useAuth();
  const navigate = useNavigate();
  const { pathname } = useLocation();
  const [showModal, setShowModal] = useState(false);  // Estado para el modal de confirmación

  const handleLogout = () => {
    sessionStorage.removeItem('cartProducts');
    logout();
    setShowModal(false);
    navigate('/home', { replace: true })
  };

  const handleLogin = () => {
    navigate('/login', { replace: true })
  }

  const handleShowModal = () => setShowModal(true);  // Mostrar el modal
  const handleCloseModal = () => setShowModal(false);  // Cerrar el modal sin hacer logout

  useEffect(() => {
    const handleResize = () => {
      setExpand(window.innerWidth < 800 ? "false" : "sm");
    };
    window.addEventListener('resize', handleResize);
    handleResize();
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  const getLinkClass = (linkPath: string) => {
    return pathname === linkPath ? 'fw-bold' : '';
  };

  return (
    <>
      <Navbar expand={expand} className="customer-navbar">
        <Container fluid>
          <Navbar.Brand href="/home">
            <i className="bi bi-house-door"></i> Frontend
          </Navbar.Brand>
          
          <Navbar.Toggle aria-controls={`offcanvasNavbar`} />
          
          <Navbar.Offcanvas
            id={`offcanvasNavbar`}
            aria-labelledby={`offcanvasNavbarLabel`}
            placement="end"
          >
            <Offcanvas.Header closeButton>
            </Offcanvas.Header>
            <Offcanvas.Body>
            <Nav 
              className="justify-content-end flex-grow-1 pe-3" 
              style={expand != 'false' ? { position: 'fixed', left: '50%', transform: 'translateX(-50%)' } : {}}
            >
                <Nav.Link onClick={() => navigate('/home',{ replace: true })} className={getLinkClass('/home')}>Home</Nav.Link>
                <Nav.Link onClick={() => navigate('/products',{ replace: true })} className={getLinkClass('/products')}>Products</Nav.Link>
                {isAuthenticated && (
                  <Nav.Link onClick={() => navigate('/orders', { replace: true })} className={getLinkClass('/orders')}>
                    Orders
                  </Nav.Link>
                )}
                <Nav.Link onClick={() => navigate('/cart',{ replace: true })} className={getLinkClass('/cart')}>
                  <i className="bi bi-cart-fill"></i>
                  {cartProducts.length > 0 && (
                    <Badge pill bg="primary">
                      {cartProducts.reduce((acum,e) => acum + e.quantity, 0)}
                    </Badge>
                  )}
                </Nav.Link >
              </Nav>
              <Nav className="justify-content-end flex-grow-1 pe-3">
                {isAuthenticated ? 
                (
                  <>
                  <span className="navbar-text me-3">{username}</span>
                  <Button variant="outline-danger" onClick={handleShowModal}>Logout</Button>
                  </>
                )
                :
                (
                  <Button variant="outline-primary" onClick={handleLogin}>Login</Button>
                )
                }
                {
                isAuthenticated && accessType == 1 && (
                  <>
                    <div className='m-1'></div>
                    <Button className="" variant="success" onClick={()=>navigate('/secure/masterproducts',{ replace: true })}>Change to admin view</Button>
                  </>
                )}
              </Nav>
            </Offcanvas.Body>
          </Navbar.Offcanvas>
        </Container>
      </Navbar>
      <Modal show={showModal} onHide={handleCloseModal}>
        <Modal.Header closeButton>
          <Modal.Title>Confirm Logout</Modal.Title>
        </Modal.Header>
        <Modal.Body>Are you sure you want to log out?</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseModal}>Cancel</Button>
          <Button variant="danger" onClick={handleLogout}>Logout</Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}

export default CustomerNavbar;
