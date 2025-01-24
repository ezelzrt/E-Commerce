import React, { useState, useEffect } from 'react';
import { getOrderDetails } from '../../services/orderService';

const OrderDetails: React.FC<{ orderId: string }> = ({ orderId }) => {
  // const [order, setOrder] = useState(null);

  // useEffect(() => {
  //   const fetchOrderDetails = async () => {
  //     const data = await getOrderDetails(orderId);
  //     setOrder(data);
  //   };
  //   fetchOrderDetails();
  // }, [orderId]);

  // if (!order) return <p>Loading...</p>;

  return (
    <></>
    // <div>
    //   <h2>Order Details for #{order.id}</h2>
    //   <ul>
    //     {order.items.map((item) => (
    //       <li key={item.productId}>
    //         {item.productName} - {item.quantity}
    //       </li>
    //     ))}
    //   </ul>
    // </div>
  );
};

export default OrderDetails;
