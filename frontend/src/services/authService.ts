import axios from 'axios';
import {API_SERVICE_URL, API_URL} from '../config';


export const login = async (credentials: { email: string; password: string }) => {
  if (!credentials.email || !credentials.password) {
    throw new Error('Both email and password are required');
  }

  try{
    const response = await axios.post(`${API_SERVICE_URL}/auth/login`, credentials);
    // const response = {status: 200, data: {message: "Amogus", refresh_token: "aaaa", access_token: "badiubawidu"}}
    

    if (response.status === 200) {
      return response.data;
    } else {
      throw new Error(`Login failed: ${response.data.message}`);
    }
  }
  catch{
    
  }
};

export const register = async (user: any) => {

  if (!user || !user.email || !user.password) {
    throw new Error('Both email and password are required');
  }

  try{
    const response = await axios.post(`${API_SERVICE_URL}/auth/register`, user);
    // const response = {
    //   status:200,
    //   data:{
    //     message:"hola",
    //     token:"abc",
    //     refresh_token:"abcdefg"
    //   }
    // }

    if (response.status === 200) {
      return response.data;
    } else {
      throw new Error(`Register failed: ${response.data.message}`);
    }
  }
  catch{
    
  }
};