import React, { useState, useEffect } from 'react';
import { getOrders } from '../../services/orderService';
import {IOrder} from '../../common/appTypes';

const OrderList: React.FC = () => {
  const [orders, setOrders] = useState<IOrder[]|null>();

  // useEffect(() => {
  //   const fetchOrders = async () => {
  //     const data = await getOrders();
  //     setOrders(data);
  //   };
  //   fetchOrders();
  // }, []);

  return (
    <></>
    // <div>
    //   <h2>Order List</h2>
    //   <ul>
    //     {orders.map((order) => (
    //       <li key={order.id}>
    //         Order #{order.id} - {order.status}
    //       </li>
    //     ))}
    //   </ul>
    // </div>
  );
};

export default OrderList;
