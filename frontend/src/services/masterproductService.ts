import axios from 'axios';
import { API_SERVICE_URL, API_URL } from '../config';
import { Attribute, AttributeValue, MasterProduct } from '../common/appTypes';

// export interface MasterProduct {
//   id: number;
//   name: string;
//   relatedProductsCount: number;
//   attributes: string[];
// }

// export const getMasterProducts = async (): Promise<MasterProduct[]> => {
//   try {
//     // const response = await axios.get(`${API_URL}/masterproducts`);
//     // return response.data; 
//     return [
//         {id: 1, name: "Shirt", relatedProductsCount: 22, attributes: ["Color","Size"]},
//         {id: 2, name: "Pants", relatedProductsCount: 13, attributes: ["Color","Size"]},
//         {id: 3, name: "Bag", relatedProductsCount: 2, attributes: ["Color"]},
//     ];
//   } catch (error) {
//     console.error('Error fetching master products:', error);
//     throw error;
//   }
// };


// export const getMasterProductAttributes = async (masterproduct_id: number): Promise<any[]> => {
//   try {
//     // const response = await axios.get(`${API_URL}/masterproducts/${masterproduct_id}/attributes`);
//     // return response.data; 

//     //Return hardcodeado de ejemplo
    
//     return masterproduct_id == 1 ?
//     [
//       { attribute_id: 1, name: "Color", values: [{ id: 1, value: "Red" }, { id: 2, value: "White" }, { id: 3, value: "Brown" }, { id: 4, value: "Black" }] },
//       { attribute_id: 2, name: "Size", values: [{ id: 1, value: "S" }, { id: 2, value: "M" }, { id: 3, value: "L" }, { id: 4, value: "XL" }, { id: 5, value: "11" }, { id: 6, value: "12" }] },
//       { attribute_id: 3, name: "Stamp", values: [{ id: 1, value: "Hulk" }, { id: 2, value: "RedGate" }] },
//     ]
//     :
//     [
//       { attribute_id: 1, name: "Color", values: [{ id: 3, value: "Brown" }, { id: 4, value: "Black" }] },
//     ]


//   } catch (error) {
//     console.error('Error fetching master product attributes:', error);
//     throw error;
//   }
// };

const sectionURL = `${API_SERVICE_URL}/masterProducts` 

export const getMasterProducts = async (): Promise<MasterProduct[]> => {
  try {
    const response = await axios.get<MasterProduct[]>(sectionURL);
    return response.data;
  } catch (error) {
    console.error('Error fetching master products:', error);
    throw error;
  }
};

export const getMasterProductById = async (id: number): Promise<MasterProduct> => {
  try {
    const response = await axios.get<MasterProduct>(`${sectionURL}/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching master product with id ${id}:`, error);
    throw error;
  }
};

export const createMasterProduct = async (request: MasterProduct): Promise<MasterProduct> => {
  try {
    const response = await axios.post<MasterProduct>(`${sectionURL}`, request);
    return response.data;
  } catch (error) {
    console.error('Error creating master product:', error);
    throw error;
  }
};

export const getAttributesByMasterProductId = async (id: number): Promise<Attribute[]> => {
  try {
    const response = await axios.get<Attribute[]>(`${sectionURL}/${id}/attributes`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching attributes for master product with id ${id}:`, error);
    throw error;
  }
};

export const getAttributesValuesByMasterProductId = async (id: number): Promise<AttributeValue[]> => {
  try {
    const response = await axios.get<AttributeValue[]>(`${sectionURL}/${id}/attributesValues`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching attribute values for master product with id ${id}:`, error);
    throw error;
  }
};