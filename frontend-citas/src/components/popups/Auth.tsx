import {useState, type FormEvent, type FC, type ChangeEvent, type MouseEvent} from 'react';
import { loginUser, registerUser } from '../../api/AuthService.ts';
import type {RegisterPayload, ErrorResponseDTO, Message, LoginResponse} from "../../types/types.ts";
import {useAuth} from "../../context/AuthContext.tsx";

import '../../assets/css/popup.css';
import {useNavigate} from "react-router-dom";


interface AuthProps {
    isPopupActive: boolean;
    toggleAuthPopup: () => void;
    message: Message | null;
    setMessage: (message: Message | null) => void;
    clearMessage: () => void;
}

const Auth: FC<AuthProps> = ({isPopupActive, toggleAuthPopup, message, setMessage, clearMessage}) => {
    const { login } = useAuth();
    const [isRegisterMode, setIsRegisterMode] = useState(false);
    const [loginCredentials, setLoginCredentials] = useState({ login: '', password: '' });
    const initialRegisterState = {
        cedula: '',
        nombre: '',
        apellido: '',
        login: '',
        password: '',
        confirmPassword: '',
        rol: 'PACIENTE' as 'PACIENTE' | 'MEDICO'
    }
    const [registerCredentials, setRegisterCredentials] = useState(initialRegisterState);
    const navigate = useNavigate();


    const toggleRegisterMode = (event?: MouseEvent<HTMLElement>) => {
        if (event) event.preventDefault();
        setIsRegisterMode(!isRegisterMode);
        clearMessage();
    };

    const handleLogin = async (event: FormEvent) => {
        event.preventDefault();
        if (!loginCredentials.login || !loginCredentials.password) {
            setMessage({ text: 'Por favor, ingresa usuario y contraseña.', type: 'error' });
            return;
        }
        setMessage({ text: 'Iniciando sesión...', type: 'info' });
        console.log('Enviando login:', loginCredentials);

        try {
            const userData: LoginResponse = await loginUser(loginCredentials);
            login(userData);
            if (userData.rol === 'MEDICO' && !userData.profileComplete) {
                navigate('/medico/completar');
            } else if (userData.rol === 'ADMIN') {
                navigate('/admin/aprobaciones');
            }

            setMessage({
                text: `
                    Sesion iniciada correctamente como ${userData.username}.
                    Cerrando modal en 3 segundos...
                `, type: 'success' });

            setLoginCredentials({ login: '', password: '' });
            setTimeout(toggleAuthPopup, 2700);
        } catch (error: any) {
            const errorResponse = error.response?.data as ErrorResponseDTO;
            const errorMessage = errorResponse?.message || error.message;
            setMessage({ text: errorMessage, type: 'error' });        }
    };

    const handleRegister = async (event: FormEvent) => {
        event.preventDefault();
        const { cedula, nombre, apellido, login, password, confirmPassword, rol } = registerCredentials;
        if (!cedula || !nombre || !apellido || !login || !password || !confirmPassword || !rol) {
            setMessage({ text: 'Por favor, completa todos los campos.', type: 'error' });
            return;
        }
        if (registerCredentials.password !== registerCredentials.confirmPassword) {
            setMessage({ text: 'Las contraseñas no coinciden.', type: 'error' });
            return;
        }
        setMessage({ text: 'Registrando...', type: 'info' });

        const userToRegister: RegisterPayload = { cedula, nombre, apellido, login, password, rol }

        console.log('Enviando registro:', userToRegister);

        try {
            const responseText = await registerUser(userToRegister);
            setMessage({
                text: `
                    ${responseText}.
                    Redirigiendo al inicio de sesion en 3 segundos...
                `, type: 'success' });
            setRegisterCredentials({ cedula: '', nombre: '', apellido: '', login: '', password: '', confirmPassword: '', rol: 'PACIENTE' });
            setTimeout(toggleRegisterMode, 2700);
        } catch (error: any) {
            const errorResponse = error.response?.data as ErrorResponseDTO;
            const errorMessage = errorResponse?.message || error.message;
            setMessage({ text: errorMessage, type: 'error' });        }
    };

    const handleLoginChange = (event: ChangeEvent<HTMLInputElement>) => {
        setLoginCredentials(prev => ({ ...prev, [event.target.name]: event.target.value }));
    };

    const handleRegisterChange = (event: ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setRegisterCredentials(prev => ({ ...prev, [event.target.name]: event.target.value }));
    };

    return (
        <>
        <div className={`wrapper ${isRegisterMode ? 'active' : ''} ${isPopupActive ? 'active-popup' : ''}`}>
            {message && (
                <div className={`message ${message.type} show`}>
                    <p>{message.text}</p>
                </div>
            )}
            <span className="icon-close" onClick={toggleAuthPopup}> X </span>

            {!isRegisterMode && (
                <div className="form-box login">
                    <h2>Iniciar Sesión</h2>
                    <form onSubmit={handleLogin}>
                        <div className="input-box">
                            <input id="login-username" type="text" name="login" value={loginCredentials.login} onChange={handleLoginChange} required />
                            <label htmlFor="login-username">Usuario</label>
                        </div>
                        <div className="input-box">
                            <input id="login-password" type="password" name="password" value={loginCredentials.password} onChange={handleLoginChange} required />
                            <label htmlFor="login-password">Contraseña</label>
                        </div>
                        <button type="submit" className="btn">Iniciar</button>
                        <div className="login-register">
                            <p>No esta registrado? <a href="#" className="register-link" onClick={toggleRegisterMode}>Registrarse</a></p>
                        </div>
                    </form>
                </div>
            )}

            {isRegisterMode && (
                <div className="form-box register">
                    <h2>Registro</h2>
                    <div className="form-container">
                        <form onSubmit={handleRegister}>
                            <div className="input-box">
                                <input id="cedula" type="text" name="cedula" value={registerCredentials.cedula}
                                       onChange={handleRegisterChange} required/>
                                <label htmlFor="cedula">Cédula</label>
                            </div>
                            <div className="input-box">
                                <input id="nombre" type="text" name="nombre" value={registerCredentials.nombre}
                                       onChange={handleRegisterChange} required/>
                                <label htmlFor="nombre">Nombre</label>
                            </div>
                            <div className="input-box">
                                <input id="apellido" type="text" name="apellido" value={registerCredentials.apellido}
                                       onChange={handleRegisterChange} required/>
                                <label htmlFor="apellido">Apellido</label>
                            </div>
                            <div className="input-box">
                                <input id="register-username" type="text" name="login" value={registerCredentials.login}
                                       onChange={handleRegisterChange} required/>
                                <label htmlFor="register-username">Usuario</label>
                            </div>
                            <div className="input-box">
                                <input id="register-password" type="password" name="password"
                                       value={registerCredentials.password}
                                       onChange={handleRegisterChange} required/>
                                <label htmlFor="register-password">Contraseña</label>
                            </div>
                            <div className="input-box">
                                <input id="confirm-password" type="password" name="confirmPassword"
                                       value={registerCredentials.confirmPassword}
                                       onChange={handleRegisterChange} required/>
                                <label htmlFor="confirm-password">Confirmar contraseña</label>
                            </div>
                            <div className="input-box">
                                <select id="rol" name="rol" value={registerCredentials.rol}
                                        onChange={handleRegisterChange}
                                        required>
                                    <option value="PACIENTE">Paciente</option>
                                    <option value="MEDICO">Médico</option>
                                </select>
                                <label htmlFor="rol" className="select-label">Soy un</label>
                            </div>
                            <button type="submit" className="btn">Registrar</button>
                        </form>
                    </div>
                    <div className="login-register">
                        <p>Ya tiene una cuenta? <a href="#" className="login-link" onClick={toggleRegisterMode}>Iniciar
                            Sesión</a></p>
                    </div>
                </div>
            )}
        </div>
        </>
    );
};

export default Auth;