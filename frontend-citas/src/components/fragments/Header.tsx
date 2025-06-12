import {type FC} from "react";
import {useAuth} from "../../context/AuthContext.tsx";
import {Link} from "react-router-dom";

interface HeaderProps {
    onLoginClick: () => void;
}

const Header: FC<HeaderProps> = ({ onLoginClick }: HeaderProps) => {
    const { user, logout } = useAuth();

    return <>
        <header>
            <Link to={'/'} className="logo">
                <img src="../../../public/img/medical-logo.jpg" alt="Logo"/>
                <span>Medical Appointments</span>
            </Link>
        <nav className="navigation">
            <Link to={'/'}>Home</Link>
                <a href="#">About</a>
                {user ? (
                    <>
                        <Link to={"/perfil"}>{user.username}</Link>
                        <button className="btnLogin-popup" onClick={logout}>Logout</button>
                    </>
                ) : (
                    <button className="btnLogin-popup" onClick={onLoginClick}>Login</button>
                )}
            </nav>
        </header>
    </>
}
export default Header;