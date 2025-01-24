import axios from 'axios';
import { API_SERVICE_URL, API_URL } from '../config';

export const getAttributes = async (): Promise<any[]> => {
  try {
    const response = await axios.get(`${API_SERVICE_URL}/attributes`);
    return response.data; 
  } catch (error) {
    console.error('Error fetching attributes:', error);
    throw error;
  }
};
