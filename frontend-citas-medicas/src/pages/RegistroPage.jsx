import { useState as useStateRP, useEffect as useEffectRP } from 'react';
import { useNavigate as useNavigateRP } from 'react-router-dom';
import { register as apiRegisterRP } from '../services/api.js';


function RegistroPage() {
    const [formData, setFormData] = useStateRP({ cedula: '', nombre: '', apellido: '', login: '', password: '', rol: 'PACIENTE' });
    const [confirmPassword, setConfirmPassword] = useStateRP('');
    const [message, setMessage] = useStateRP('');
    const navigate = useNavigateRP();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (formData.password !== confirmPassword) {
            setMessage('Las contraseñas no coinciden.');
            return;
        }
        setMessage('');
        const result = await apiRegisterRP(formData);
        if (result.success) {
            alert('Registro exitoso. Ahora puede iniciar sesión.');
            navigate('/login');
        } else {
            setMessage(result.message);
        }
    };

    return (
        <div className="main">
            <h1>Formulario de registro</h1>
            {message && <div style={{ color: 'red', marginBottom: '1rem' }}>{message}</div>}
            <form onSubmit={handleSubmit}>
                <label>Cédula:</label><input type="text" name="cedula" value={formData.cedula} onChange={handleChange} required />
                <label>Nombre:</label><input type="text" name="nombre" value={formData.nombre} onChange={handleChange} required />
                <label>Apellido:</label><input type="text" name="apellido" value={formData.apellido} onChange={handleChange} required />
                <label>Login:</label><input type="text" name="login" value={formData.login} onChange={handleChange} required />
                <label>Password:</label><input type="password" name="password" value={formData.password} onChange={handleChange} required />
                <label>Confirmar Password:</label><input type="password" value={confirmPassword} onChange={e => setConfirmPassword(e.target.value)} required />
                <label>Rol:</label>
                <select name="rol" value={formData.rol} onChange={handleChange}>
                    <option value="PACIENTE">Paciente</option>
                    <option value="MEDICO">Médico</option>
                </select>
                <div className="wrap"><button type="submit">Registrar</button></div>
            </form>
        </div>
    );
}

export default RegistroPage;