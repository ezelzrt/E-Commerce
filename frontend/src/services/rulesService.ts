import { Rule } from '../common/appTypes'; // Asegúrate de tener esta definición de tipos

// Función para obtener las reglas desde el backend
export const getRules = async (): Promise<Rule[]> => {
  try {
    const response = await fetch('/api/rules'); // Reemplaza con la URL correcta de tu API
    if (!response.ok) {
      throw new Error('Failed to fetch rules');
    }
    const data = await response.json();
    return data.rules; // Asegúrate de que la respuesta del backend tenga un objeto con las reglas
  } catch (error) {
    console.error('Error fetching rules:', error);
    throw error; // Lanza el error para que lo puedas manejar en la página
  }
};

// Función para actualizar el estado de activación de una regla
export const toggleRuleActive = async (ruleId: number): Promise<void> => {
  try {
    const response = await fetch(`/api/rules/${ruleId}/toggle`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ ruleId }),
    });
    if (!response.ok) {
      throw new Error('Failed to toggle rule active state');
    }
  } catch (error) {
    console.error('Error toggling rule active:', error);
    throw error;
  }
};

// Función para editar una regla
export const editRule = async (ruleId: number, updatedRule: Rule): Promise<Rule> => {
  try {
    const response = await fetch(`/api/rules/${ruleId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(updatedRule),
    });
    if (!response.ok) {
      throw new Error('Failed to edit rule');
    }
    const data = await response.json();
    return data.updatedRule; // Asegúrate de que la respuesta del backend tenga la regla actualizada
  } catch (error) {
    console.error('Error editing rule:', error);
    throw error;
  }
};