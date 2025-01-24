import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Breadcrumb from 'react-bootstrap/Breadcrumb'
import { useNavigate, useLocation } from 'react-router-dom';
import { Alert, Button, Col, Container, Row, Stack, Table } from 'react-bootstrap';
import Image from 'react-bootstrap/Image';
import {getOrderDetails, setOrderStatus} from '../services/orderService'
import { useAuth } from '../contexts/AuthContext';

enum OrderStatus {
    ENTERED = 0,
    CONFIRMED = 1,
    IN_PROGRESS = 2,
    DELIVERED = 3,
    CANCELLED = 4
}

function getOrderStatusString(status: number): string {
    return OrderStatus[status]; // Usa el valor para obtener la clave del enum
}

const OrderDetailsPage: React.FC = () => {
    
    const { orderID } = useParams();
    let orderIdInt = parseInt(orderID ?? "0", 10);
    const location = useLocation();
    const [isAdmin, setIsAdmin] = useState<boolean>(location.pathname.startsWith("/secure/orders"));
    const [error, setError] = useState<string>('');

    const navigate = useNavigate();
    const {isAuthenticated, accessType } = useAuth();
    const [order, setOrder] = useState<any>([]);

    const fetchOrder = async () => {
        try {
            const response = await getOrderDetails(parseInt(orderID ?? "0", 10));
            setOrder(response);
        } catch (error) {
            navigate('/orders');  
            console.error('Error fetching orders:', error);
        }
    };

    useEffect(() => {
        if (!isAuthenticated) {
            navigate('/home');  
        } else {
            fetchOrder();
        }
    }, [isAuthenticated, navigate]); 

    const handleChangeStatus = async (newStatus: number): Promise<void> => {
        try{

            const response = await setOrderStatus(order.orderID, newStatus);
            setOrder({
                ...order,  
                orderStatus: getOrderStatusString(newStatus)
            });
            setError('');
        }   
        catch(err: any){
            setError(err.message)
        }
    }

    const actions: any[] = [];

    const setActions = () => {
        if(
            (
            !isAdmin 
            &&
                (
                    (order.orderStatus == "ENTERED") 
                    ||
                    (order.orderStatus == "CONFIRMED" && true) //Reemplazar true por la lógica de si lleva menos de 24 hs confirmada 
                )
            )
            ||
            (
            isAdmin
            &&
            (
                (order.orderStatus == "IN_PROGRESS" || order.orderStatus == "CONFIRMED") 
            )
            )
        ){
            actions.push(
                (<Button onClick={() => handleChangeStatus(OrderStatus.CANCELLED)} key={"CANCEL"} variant="outline-danger">Cancel</Button>)
            )
        }
        
        switch(order.orderStatus)
        {
            
            case "ENTERED":
                if(!isAdmin) actions.push((<Button onClick={() => handleChangeStatus(OrderStatus.CONFIRMED)} key={"ENTERED"} variant="success">Confirm</Button>))
                break;
            case "CONFIRMED":
                if(isAdmin) actions.push((<Button onClick={() => handleChangeStatus(OrderStatus.IN_PROGRESS)} key={"CONFIRMED"} variant="info">Mark as in progress</Button>))
                break;
            case "IN_PROGRESS":
                if(isAdmin) actions.push((<Button onClick={() => handleChangeStatus(OrderStatus.DELIVERED)} key={"IN_PROGRESS"} variant="primary">Mark as delivered</Button>))
                break;
        }
    }

    setActions();
    
  return (
    <div>
        <Breadcrumb>
            <Breadcrumb.Item onClick={() => navigate('/orders',{ replace: true })}>Orders</Breadcrumb.Item>
            <Breadcrumb.Item active>Order #{order.orderID?.toString().padStart(5,'0')}</Breadcrumb.Item>
        </Breadcrumb>
        <h1>Order #{order.orderID?.toString().padStart(5,'0')}</h1>
        {error && 
            <Alert variant={'danger'}>
            {error}
            </Alert>
        }

        <Container fluid className="m-0">
            <Row>
                {/* <Col xs={9}> */}
                <Col lg={8} md={12} sm={12} className='mb-3'>
                    <Stack gap={3}>
                        <div className="p-2 bg-light-gray rounded">
                            <h2>Details</h2>
                            <Table>
                                <tbody>
                                    <tr>
                                        <th>Status</th>
                                        <td>{order.orderStatus?.toLowerCase().replace("_", " ")}</td>
                                    </tr>
                                    <tr>
                                        <th>Deliver</th>
                                        <td>{order.deliver}</td>
                                    </tr>
                                </tbody>
                            </Table>
                        </div>
                        <div className="p-2 bg-light-gray rounded">
                            <h2>Products</h2>
                            <Table striped>
                                <thead>
                                    <tr>
                                        <th>Name</th>
                                        <th className="text-center">Quantity</th>
                                        <th className="text-end">Unit Price</th>
                                        <th className="text-end">Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {order.details?.map((product: any, index: number) => (
                                        <tr key={index} className="align-middle">
                                            <td>{product.name}</td>
                                            <td className="text-center">{product.quantity}</td>
                                            <td className="text-end">${product.unitPrice.toFixed(2)}</td>
                                            <td className="text-end">${(product.quantity * product.unitPrice).toFixed(2)}</td>
                                        </tr>
                                    ))}
                                </tbody>
                                <tfoot>
                                    <tr>
                                    <th>Total</th>
                                    <td className="text-center">{order.details?.reduce((acum: number, e: any) => acum + e.quantity, 0)}</td>
                                    <td className="text-end">${order.details?.reduce((acum: number, e: any) => acum + e.unitPrice, 0).toFixed(2)}</td>
                                    <td className="text-end">${order.details?.reduce((acum: number, e: any) => acum + e.unitPrice * e.quantity, 0).toFixed(2)}</td>
                                    </tr>
                                </tfoot>
                            </Table>
                        </div>
                    </Stack>
                </Col>
                {/* <Col xs={3}> */}
                <Col lg={4} md={12} sm={12}>
                    <Stack gap={3}>
                        <div className="p-2 bg-light-gray rounded">
                            <h2 className='text-center'>Personal information</h2>
                            <div className="text-center mb-2">
                                <Image src="/images/person-circle.svg" roundedCircle style={{ width: '100px', height: '100px' }} />
                            </div>
                            <Table>
                                <tbody>
                                    <tr>
                                        <th>Name</th>
                                        <td>{order.customer?.first_name}</td>
                                    </tr>
                                    <tr>
                                        <th>Last name</th>
                                        <td>{order.customer?.last_name}</td>
                                    </tr>
                                    <tr>
                                        <th><i className="bi bi-envelope-fill"></i> Email</th>
                                        <td>{order.customer?.email}</td>
                                    </tr>
                                </tbody>
                            </Table>
                        </div>
                        {actions.length > 0 ? 
                        (
                            <>
                                <div className="p-2 bg-light-gray rounded">
                                <h2 className='text-center'>Actions</h2>
                                <Stack gap={3}>
                                    {actions.map(action => action)}
                                </Stack>
                                </div>
                            </>
                        )
                        :
                        (<></>)
                        }
                    </Stack>
                </Col>
            </Row>
        </Container>
    </div>
  );
};

export default OrderDetailsPage;
