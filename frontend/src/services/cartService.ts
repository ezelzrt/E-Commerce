import { CartProduct } from "../common/appTypes";
import { API_SERVICE_URL } from "../config";
import axios from 'axios';

// Hardcodeamos los productos del carrito como ejemplo
let cartProducts: CartProduct[] = [
    { name: 'Producto A', quantity: 2, unitPrice: 15.99 },
    { name: 'Producto B', quantity: 1, unitPrice: 25.5 },
    { name: 'Producto C', quantity: 3, unitPrice: 9.99 },
];

// Función para obtener los productos del carrito
export const getCartProducts = async (): Promise<CartProduct[]> => {
    // Aquí se podría hacer un request al backend para obtener los productos del carrito
    // Ejemplo con Axios (comentado por ahora):
    // const response = await axios.get(`${API_URL}/cart`);
    // return response.data; // Suponiendo que el backend devuelve los productos en un array

    return cartProducts; // Retorna los productos hardcodeados por ahora
};

// Función para simular la confirmación de compra
export const confirmPurchase = async (products: any[]): Promise<string> => {
    try {
        // Aquí se haría el request real al backend para confirmar la compra
        const formattedProduct = products.map(product => {
            return {
                productId: product.id,
                amount: product.quantity
            }
        })
        const response = await axios.post(`${API_SERVICE_URL}/orders`, formattedProduct, {
            headers: {
              'Authorization': `Bearer ${sessionStorage.getItem('access_token')}`
            }
          });
          if (response.status === 200) {
            return 'Order created successfully!';
        } else {
            throw new Error('Unexpected response from server');
        }
    } catch (error: any) {
        if (error.response) {
            const errorMessage = error.response.data?.message || 'An error occurred on the server';
            throw new Error(errorMessage); 
        } else if (error.request) {
            throw new Error('No response from server. Please check your network connection.');
        } else {
            throw new Error('An unexpected error occurred: ' + error.message);
        }
    }
};