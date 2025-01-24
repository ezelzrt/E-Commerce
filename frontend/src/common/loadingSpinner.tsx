import React from 'react';
import { Circles } from 'react-loader-spinner';

interface LoadingSpinnerProps {
  height?: string;
  width?: string;
  color?: string;
}

const LoadingSpinner: React.FC<LoadingSpinnerProps> = ({
  height = "80",
  width = "80",
  color = "#4fa94d",
}) => {
  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '50vh' }}>
      <Circles
        height={height}
        width={width}
        color={color}
        ariaLabel="loading"
      />
    </div>
  );
};

export default LoadingSpinner;