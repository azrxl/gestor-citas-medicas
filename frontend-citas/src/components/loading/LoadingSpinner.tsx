import type {FC} from "react";

interface LoadingSpinnerProps {
    size?: number; // Tamaño del spinner en píxeles
    color?: string; // Color del borde del spinner
}

const LoadingSpinner: FC<LoadingSpinnerProps> = ({size = 40, color = '#000000',}) => {
    return <div className="loading-spinner"
                style={{
                    width: `${size}px`,
                    height: `${size}px`,
                    border: `4px solid ${color}`,
                    borderTop: `4px solid transparent`,
                }}
    />
};

export default LoadingSpinner;