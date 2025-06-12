// src/AppLayout.tsx

import { useState } from "react";
import { Outlet } from "react-router-dom"; // El cambio más importante
import Header from "./components/fragments/Header.tsx";
import Auth from "./components/popups/Auth.tsx";
import Footer from "./components/fragments/Footer.tsx";
import type { Message } from "./types/types.ts";
import type {ConfirmCitaResponseDTO} from "./types/citaTypes.ts";
import { confirmarCita } from "./api/citaService.ts";
import {ConfirmarCita} from "./components/popups/ConfirmarCita.tsx";
import { PopupProvider } from "./context/PopupContext.tsx";

function AppLayout() {
    // Toda esta lógica de estado se queda aquí, porque controla
    // componentes que son parte del "marco" (Header y Auth popup)
    const [message, setMessage] = useState<Message | null>(null);
    const clearMessage = () => setMessage(null);
    const [isAuthPopupActive, setIsAuthPopupActive] = useState(false);

    const toggleAuthPopup = () => {
        setIsAuthPopupActive(!isAuthPopupActive);
        if (isAuthPopupActive) {
            setTimeout(clearMessage, 500);
        }
    };
    const [isCitaPopupOpen, setIsCitaPopupOpen] = useState(false);
    const [citaSeleccionada, setCitaSeleccionada] = useState<ConfirmCitaResponseDTO | null>(null);

    const abrirCitaPopup = (info: ConfirmCitaResponseDTO) => {
        setCitaSeleccionada(info);
        setIsCitaPopupOpen(true);
    };

    const cerrarCitaPopup = () => {
        setIsCitaPopupOpen(false);
        setCitaSeleccionada(null);
    };

    const handleConfirmarCita = async (citaId: number) => {
        try {
            await confirmarCita(citaId);
            alert('¡Cita reservada con éxito!');
            cerrarCitaPopup();
            // Idealmente, aquí se debería actualizar la lista de citas en Home
            // para que el espacio aparezca como no disponible.
            window.location.reload(); // Solución simple por ahora
        } catch (error: any) {
            alert(`Error al reservar la cita: ${error.message}`);
        }
    };

    return (
        <>
            <PopupProvider value={{ abrirCitaPopup }}>
                <Header onLoginClick={toggleAuthPopup} />
                <main>
                    {/* Aquí es donde se renderizará HomePaciente o PerfilMedico */}
                    <Outlet />
                </main>
                <Auth
                    isPopupActive={isAuthPopupActive}
                    toggleAuthPopup={toggleAuthPopup}
                    message={message}
                    setMessage={setMessage}
                    clearMessage={clearMessage}
                />
                <ConfirmarCita
                    isOpen={isCitaPopupOpen}
                    onClose={cerrarCitaPopup}
                    onConfirm={handleConfirmarCita}
                    citaInfo={citaSeleccionada}
                />
                <Footer />
            </PopupProvider>
        </>
    );
}

export default AppLayout;