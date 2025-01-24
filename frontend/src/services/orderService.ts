import axios from 'axios';  // Si usas Axios para hacer peticiones HTTP
import { IOrder, CartProduct, Product } from "../common/appTypes";
import {API_SERVICE_URL} from '../config';


// Función para obtener las órdenes
export const getOrders = async (isAdmin: boolean): Promise<any[]> => {
  try {
    const role = isAdmin ? 'admin' : 'client';
    const response = await axios.get(`${API_SERVICE_URL}/orders`, {
      headers: {
        'Authorization': `Bearer ${sessionStorage.getItem('access_token')}`
      },
      params: {
        role
      }
    });
    return response.data;  
  } catch (error) {
    console.error('Error al obtener las órdenes:', error);
    throw new Error('Error al obtener las órdenes');
  }
};

// Función para obtener los detalles de una orden específica
export const getOrderDetails = async (orderID: number): Promise<IOrder | null> => {
  try {
    const response = await axios.get(`${API_SERVICE_URL}/orders/${orderID}`, {
      headers: {
        'Authorization': `Bearer ${sessionStorage.getItem('access_token')}`
      }
    });
    return response.data; 
  } catch (error) {
    console.error('Error al obtener los detalles de la orden:', error);
    throw new Error('Error al obtener los detalles de la orden');
  }
};


export const setOrderStatus = async (orderID: number, newStatus: number): Promise<any> => {
  try {
    const response = await axios.patch(`${API_SERVICE_URL}/orders/${orderID}/status`, 
      {
        newStatus
      },
      {
        headers: {
          'Authorization': `Bearer ${sessionStorage.getItem('access_token')}`
        }
      }
    );
  } catch (error: any) {
    console.error('Error updating the order:', error);
    throw new Error(error.response.data.message || error.response.message || 'Error updating the order');
  }
};