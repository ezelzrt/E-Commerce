import React, { useEffect, useState } from 'react';
import { Table, Button, Alert, Breadcrumb } from 'react-bootstrap';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import {getOrders} from '../services/orderService'
import { formatDateTime } from '../utils/dateHelper';
const OrderPage: React.FC = () => {
  const {isAuthenticated, accessType } = useAuth();
  const [orders, setOrders] = useState<any>([]); //Array<{ orderID: number; productCount: number; total: number }>
  const navigate = useNavigate();
  const location = useLocation();
  const [isAdmin, setIsAdmin] = useState<boolean>(location.pathname.startsWith("/secure/orders"));

  const fetchOrders = async () => {
    try {
      const rawResponse = await getOrders(isAdmin);
      const response = rawResponse.map(e => ({
        orderID: e.orderID,
        productCount: e.orderDetails?.reduce((acum: number, d: any) => acum + d.product_amount, 0) ?? 0,
        total: e.orderDetails?.reduce((acum: number, d: any) => acum + d.product_price * d.product_amount, 0) ?? 0,
        customer: e.customer,
        status: e.status.toLowerCase().replace("_", " "),
        date: e.date
      }))

      setOrders(response);
    } catch (error) {
      console.error('Error fetching orders:', error);
    }
  };

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/home');  
    } else {
      fetchOrders();
    }
  }, [isAuthenticated, navigate]); 

  const handleViewDetails = (orderID: number) => {
    if(isAdmin)
      navigate(`/secure/orders/${orderID}`);
    else
      navigate(`/orders/${orderID}`);
  };

  return (
    <div>
      {isAdmin ?
      (
        <>
          <Breadcrumb>
              <Breadcrumb.Item active>Commercial</Breadcrumb.Item>
              <Breadcrumb.Item active>Orders</Breadcrumb.Item>
          </Breadcrumb>
          <h1>Orders</h1>
        </>
      )
      :
      (
        <>
          <Breadcrumb>
              <Breadcrumb.Item active>Orders</Breadcrumb.Item>
          </Breadcrumb>
          <h1>My Orders</h1>
        </>
      )
      }
      {orders.length > 0 ? (
        <Table striped>
          <thead>
            <tr>
              <th className="text-center">Order Number</th>
              <th className="text-center">Date</th>
              {isAdmin && (
                <th>Customer</th>
              )}
              <th className="text-center">Status</th>
              <th className="text-center">Product Count</th>
              <th className="text-end">Total</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order : any) => (
              <tr key={order.orderID} className="align-middle">
                <td className="text-center">
                  #{order.orderID?.toString().padStart(5, '0')}
                </td>
                <td className="text-center">{formatDateTime(order.date)}</td>
                {isAdmin && (
                  <td>{`[${order.customer.id}] - ${order.customer.firstname} ${order.customer.lastname}`}</td>
                )}
                <td className="text-center">{order.status}</td>
                <td className="text-center">{order.productCount}</td>
                <td className="text-end">${order.total.toFixed(2)}</td>
                <td className="text-center">
                  <Button variant="outline-primary" onClick={() => handleViewDetails(order.orderID)}>
                    <i className="bi bi-file-earmark-fill"></i> View Details
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      ) : 
      isAdmin ?
      (
        <Alert variant="info">
          No purchase orders were found.
        </Alert>
      )
      :
      (
        <Alert variant="info">
          You have no orders yet.{' '}
          <span
            style={{ color: 'blue', cursor: 'pointer' }}
            onClick={() => navigate('/products')}
          >
            Start shopping now!
          </span>
        </Alert>
      )
    }
    </div>
  );
};

export default OrderPage;