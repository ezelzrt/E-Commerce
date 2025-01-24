import { showErrorToast } from './toastNotification';

export const apiRequestHandler = async <T>(
  apiCall: () => Promise<T>,
  setData: (data: T) => void,
  filter?: (f: any) => any
): Promise<void> => {
  try {
    const data = await apiCall();
    if(filter)
      setData(filter(data));
    else
      setData(data);

  } catch (err) {
    const errorMessage = err instanceof Error ? err.message : 'An unexpected error occurred';
    showErrorToast(errorMessage);
  }
};