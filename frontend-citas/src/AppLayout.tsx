// src/AppLayout.tsx
import { useState, useEffect } from "react";
import { Outlet } from "react-router-dom";
import Header from "./components/fragments/Header.tsx";
import Auth from "./components/popups/Auth.tsx";
import Footer from "./components/fragments/Footer.tsx";
import type { Message } from "./types/types.ts";
import type { ConfirmCitaResponseDTO } from "./types/citaTypes.ts";
import { confirmarCita } from "./api/citaService.ts";
import { ConfirmarCita } from "./components/popups/ConfirmarCita.tsx";
import { PopupProvider } from "./context/PopupContext.tsx";
import { useAuth } from "./context/AuthContext.tsx";

function AppLayout() {
    const { user } = useAuth();

    // — Estado para mensajes de Auth
    const [message, setMessage] = useState<Message | null>(null);
    const clearMessage = () => setMessage(null);

    // — Control del popup de Auth
    const [isAuthPopupActive, setIsAuthPopupActive] = useState(false);

    // — Control del popup de Confirmar Cita
    const [isCitaPopupOpen, setIsCitaPopupOpen] = useState(false);
    const [citaSeleccionada, setCitaSeleccionada] = useState<ConfirmCitaResponseDTO | null>(null);

    // — Aquí guardamos la cita que el usuario quiso confirmar antes de loguearse
    const [pendingConfirmId, setPendingConfirmId] = useState<number | null>(null);

    // Función que abre el popup de cita (se llama desde Home o donde sea)
    const abrirCitaPopup = (info: ConfirmCitaResponseDTO) => {
        setCitaSeleccionada(info);
        setIsCitaPopupOpen(true);
    };
    const cerrarCitaPopup = () => {
        setIsCitaPopupOpen(false);
        setCitaSeleccionada(null);
    };

    // Cuando se da click a "Confirmar" en el popup de cita:
    const handleConfirmClick = (citaId: number) => {
        if (!user) {
            // 1) Guardamos la cita pendiente y abrimos login
            setPendingConfirmId(citaId);
            setIsAuthPopupActive(true);
            setIsCitaPopupOpen(false);
        } else {
            // 2) Si ya hay user, confirmamos de una
            ejecutarConfirm(citaId);
        }
    };

    // Efecto: cuando cambie `user`, si acabas de loguearte y había una cita pendiente, la confirmas
    useEffect(() => {
        if (user && pendingConfirmId !== null) {
            ejecutarConfirm(pendingConfirmId);
            setPendingConfirmId(null);
        }
    }, [user]);

    // Al cerrar el popup de Auth: si NO hay user y había pendiente, cancelamos todo
    const toggleAuthPopup = () => {
        const wasOpen = isAuthPopupActive;
        setIsAuthPopupActive(!isAuthPopupActive);

        if (wasOpen) {
            if (!user && pendingConfirmId !== null) {
                // El usuario cerró sin loguearse: cancelamos
                setPendingConfirmId(null);
                cerrarCitaPopup();
            }
            setTimeout(clearMessage, 500);
        }
    };

    // Lógica real de confirmar cita
    const ejecutarConfirm = async (citaId: number) => {
        try {
            await confirmarCita(citaId);
            alert("¡Cita confirmada con éxito!");
            cerrarCitaPopup();
            window.location.reload(); // o mejor refetch de datos
        } catch (err: any) {
            alert(`Error al confirmar la cita: ${err.message}`);
        }
    };

    return (
        <>
            <PopupProvider value={{ abrirCitaPopup }}>
                <Header onLoginClick={toggleAuthPopup} />
                <main>
                    <Outlet />
                </main>

                {/* POPUP de Auth */}
                <Auth
                    isPopupActive={isAuthPopupActive}
                    toggleAuthPopup={toggleAuthPopup}
                    message={message}
                    setMessage={setMessage}
                    clearMessage={clearMessage}
                />

                {/* POPUP de Confirmar Cita */}
                <ConfirmarCita
                    isOpen={isCitaPopupOpen}
                    onClose={cerrarCitaPopup}
                    onConfirm={handleConfirmClick}
                    citaInfo={citaSeleccionada}
                />

                <Footer />
            </PopupProvider>
        </>
    );
}

export default AppLayout;
