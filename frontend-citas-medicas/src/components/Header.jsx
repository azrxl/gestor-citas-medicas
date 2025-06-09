import { Link, NavLink } from 'react-router-dom';
import logo from '../assets/images/medical.jpg';

function Header({ user, onLogout }) {
    return (
        <header className="main-header-react">
            <div className="left">
                <img src={logo} alt="Logo" />
                <Link to="/" className="header-title">Medical Appointments</Link>
            </div>
            <nav className="right">
                <NavLink to="/" className={({isActive}) => isActive ? "header-button active" : "header-button"}>Home</NavLink>
                {user ? (
                    <>
                        {user.rol === 'ADMIN' && <NavLink to="/admin" className={({isActive}) => isActive ? "header-button active" : "header-button"}>Aprobar Médicos</NavLink>}
                        {(user.rol === 'MEDICO' || user.rol === 'PACIENTE') && <NavLink to="/perfil" className={({isActive}) => isActive ? "header-button active" : "header-button"}>Mi Perfil</NavLink>}
                        <span className="header-username">{user.nombre}</span>
                        <button onClick={onLogout} className="header-button-logout">Logout</button>
                    </>
                ) : (
                    <>
                        <NavLink to="/login" className={({isActive}) => isActive ? "header-button active" : "header-button"}>Login</NavLink>
                        <NavLink to="/registro" className={({isActive}) => isActive ? "header-button active" : "header-button"}>Registro</NavLink>
                    </>
                )}
            </nav>
        </header>
    );
}

export default Header;