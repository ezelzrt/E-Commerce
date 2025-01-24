export interface Attribute {
    id: number;
    name: string;
    description: string;
  }
  
  export interface MasterProduct {
    id: number;
    name: string;
    description: string;
    attributes: Attribute[];
    attributeValues: AttributeValue[];
    products: Product[];
    basePrice: number;
  }
  
  
  export interface AttributeValue {
    id: number;
    value: string;
    name?: string;
    attribute?: Attribute;
  }

  export interface Product {
    id?: number;
    masterproduct: any,
    name: string;
    stock: number;
    imageURL: string;
    attributeValues: AttributeValue[];
  }
  
  export interface ICreateProduct {
    masterproductid: number,
    name: string;
    stock: number;
    attributes: { id: number; value: string }[];
  }

  export interface CartProduct {
    name: string;
    quantity: number;
    unitPrice: number;
  }

  export interface IOrder {
    id: number;
    totalUnitQty: number;
    totalPrice: number;
    orderDate: string;
    status: string;
  }

  export interface Rule {
    id: number;
    number: string; // Número de la regla, por ejemplo "#0001"
    name: string;   // Nombre de la regla
    type: 'Masterproduct' | 'Product'; // Tipo de cosa (Masterproduct o Product)
    condition: 'Greater' | 'Lesser' | 'GreaterOrEqual' | 'LesserOrEqual'; // Condición: Mayor, Menor, Mayor o Igual, Menor o Igual
    conditionValue: number; // Valor para la condición
    isActive: boolean; // Estado de activación de la regla (On/Off)
  }