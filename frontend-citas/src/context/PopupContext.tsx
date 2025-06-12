import { createContext, useContext, type ReactNode } from 'react';
import type { ConfirmCitaResponseDTO } from "../types/citaTypes.ts";

// Definimos la forma de la información que necesita el popup

interface PopupContextType {
    // Función para abrir el popup de confirmación de cita
    abrirCitaPopup: (info: ConfirmCitaResponseDTO) => void;
}

const PopupContext = createContext<PopupContextType | null>(null);

// Hook personalizado para usar el contexto fácilmente
export const usePopups = (): PopupContextType => {
    const context = useContext(PopupContext);
    if (!context) {
        throw new Error('usePopups debe ser usado dentro de un PopupProvider');
    }
    return context;
};

// El componente Proveedor que usaremos en AppLayout
export const PopupProvider = ({ children, value }: { children: ReactNode, value: PopupContextType }) => {
    return (
        <PopupContext.Provider value={value}>
            {children}
        </PopupContext.Provider>
    );
};