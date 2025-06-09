import { useState as useStateLP, useEffect as useEffectLP } from 'react';
import { Link as LinkLP, useNavigate as useNavigateLP } from 'react-router-dom';

function LoginPage({ onLogin }) {
    const [loginId, setLoginId] = useStateLP('');
    const [password, setPassword] = useStateLP('');
    const [message, setMessage] = useStateLP('');
    const navigate = useNavigateLP();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');
        const result = await onLogin(loginId, password);
        if (!result.success) {
            setMessage(result.message);
        } else {
            navigate('/'); // Redirige al home en caso de éxito
        }
    };

    return (
        <div className="main">
            <h1>Iniciar Sesión</h1>
            {message && <div style={{ color: 'red', marginBottom: '1rem' }}>{message}</div>}
            <form onSubmit={handleSubmit}>
                <label htmlFor="loginId">Usuario:</label>
                <input id="loginId" type="text" value={loginId} onChange={e => setLoginId(e.target.value)} required />
                <label htmlFor="password">Contraseña:</label>
                <input id="password" type="password" value={password} onChange={e => setPassword(e.target.value)} required />
                <div className="wrap">
                    <button type="submit">Ingresar</button>
                </div>
            </form>
            <p>¿No está registrado? <LinkLP to="/registro">Registrarse</LinkLP></p>
        </div>
    );
}

export default LoginPage;