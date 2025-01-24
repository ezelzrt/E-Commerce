import { toast } from 'react-toastify';

export const showErrorToast = (message: string) => {
  toast.error(message, {
    position: "bottom-left",
    autoClose: 5000,
    closeOnClick: true,
    pauseOnHover: true,
    draggable: true,
    style: { backgroundColor: '#ff4d4f', color: '#fff' }, // Red background, white text
  });
};