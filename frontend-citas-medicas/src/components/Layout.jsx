import { Outlet } from 'react-router-dom';
import Header from './Header.jsx';
import Footer from './Footer.jsx';

function Layout({ user, onLogout }) {
    return (
        <div className="app-container">
            <Header user={user} onLogout={onLogout} />
            <main className="main-content">
                <Outlet />
            </main>
            <Footer />
        </div>
    );
}

export default Layout;