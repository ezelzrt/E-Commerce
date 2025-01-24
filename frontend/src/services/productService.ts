import axios from 'axios';
import { API_SERVICE_URL, API_URL } from '../config';
import { ICreateProduct, Product } from '../common/appTypes';

// export const getProducts = async (): Promise<Product[]> => {
//   try {
//     // const response = await axios.get(`${API_URL}/products`);
//     // return response.data; 
//     return [
//         {id: 1, masterproductid: 1, name: "Green shirt M", stock: 11},
//         {id: 2, masterproductid: 1, name: "Green shirt S", stock: 14},
//         {id: 3, masterproductid: 1, name: "Green shirt L", stock: 6},
//         {id: 4, masterproductid: 2, name: "Red shirt L", stock: 15},
//         {id: 5, masterproductid: 2, name: "Red shirt L", stock: 6},
//         {id: 6, masterproductid: 2, name: "Red shirt L", stock: 10},
//     ];
//   } catch (error) {
//     console.error('Error fetching products:', error);
//     throw error;
//   }
// };

// export const createProduct = async (product: ICreateProduct) => {
//   const response = await axios.post(`${API_URL}/products`, product);
//   return response.data;
// };

// export const updateStock = async (productId: string, stock: number) => {
//   const response = await axios.put(`${API_URL}/products/${productId}/stock`, { stock });
//   return response.data;
// };
const sectionURL = `${API_SERVICE_URL}/products`;

export const createProduct = async (product: ICreateProduct): Promise<Product> => {
  try {
    const response = await axios.post<Product>(sectionURL, product, {
      headers: {
        'Authorization': `Bearer ${sessionStorage.getItem('access_token')}`
      }
    });
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const deleteProduct = async (id: number): Promise<void> => {
  try {
    await axios.delete(`${sectionURL}/${id}`);
  } catch (error) {
    console.error('Error deleting product:', error);
    throw error;
  }
};

export const getProducts = async (): Promise<Product[]> => {
  try {
    const response = await axios.get<Product[]>(sectionURL);
    return response.data;
  } catch (error) {
    console.error('Error fetching products:', error);
    throw error;
  }
};

export const getProductById = async (id: number): Promise<Product | null> => {
  try {
    const response = await axios.get<Product>(`${sectionURL}/${id}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching product by ID:', error);
    return null;
  }
};

export const getProductsByMasterId = async (masterId: number): Promise<Product[]> => {
  try {
    const response = await axios.get<Product[]>(`${sectionURL}/search`, { params: { masterProduct: masterId } });
    return response.data;
  } catch (error) {
    console.error('Error fetching products by master product ID:', error);
    throw error;
  }
};

export const getStockProductById = async (id: number): Promise<number | null> => {
  try {
    const response = await axios.get<number>(`${sectionURL}/${id}/stock`);
    return response.data;
  } catch (error) {
    console.error('Error fetching stock for product:', error);
    throw error;
  }
};

export const patchStockProduct = async (id: number, addStock: number): Promise<number | null> => {
  try {
    const response = await axios.patch<number>(`${sectionURL}/${id}/stock`, { addStock });
    return response.data;
  } catch (error) {
    console.error('Error updating stock for product:', error);
    throw error;
  }
};

export const patchCompromisedStockProduct = async (id: number, addCompromisedStock: number): Promise<number | null> => {
  try {
    const response = await axios.patch<number>(`${sectionURL}/${id}/compromisedStock`, { addCompromisedStock });
    return response.data;
  } catch (error) {
    console.error('Error updating compromised stock for product:', error);
    throw error;
  }
};
