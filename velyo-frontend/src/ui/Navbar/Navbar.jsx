import React, { useMemo, useState } from "react";
import { useAuth } from "../../auth/hook/useAuth";
import {
  CalendarOutlined,
  HeartOutlined,
  UserAddOutlined,
  UserOutlined,
} from "@ant-design/icons";
import { IoLogOutOutline } from "react-icons/io5";
import { LuLayoutDashboard } from "react-icons/lu";
import { Link } from "react-router-dom";
import { Avatar, Dropdown } from "antd";
import Isologo from "../../assets/images/velyo_logotipo.svg"
import "./Navbar.css"
export const Navbar = () => {
    const { user, logout } = useAuth();
    const [menuOpen, setMenuOpen] = useState(false);

    const handleClick = () => {
        setMenuOpen(prev => !prev);
    };

    const handleLogout = () => {
        logout();
    };

    // Navegación común
    const commonNavItems = [
        {
            path: '/favoritos',
            icon: <HeartOutlined />,
            label: 'Favoritos',
            roles: ['USER', 'ADMIN']
        },
        {
            path: '/perfil',
            icon: <UserOutlined />,
            label: 'Perfil',
            roles: ['USER', 'ADMIN']
        },
        {
            path: '/mis-reservas',
            icon: <CalendarOutlined />,
            label: 'Reservas',
            roles: ['USER', 'ADMIN']
        }
    ];

    const adminNavItem = {
        path: '/administracion',
        icon: <LuLayoutDashboard />,
        label: 'Administración',
        roles: ['ADMIN']
    };

    const logoutItem = {
        path: '/',
        icon: <IoLogOutOutline />,
        label: 'Cerrar Sesión',
        action: handleLogout,
        roles: ['USER', 'ADMIN']
    };

    // Menú desplegable del avatar
    const menuItems = useMemo(() => {
        const items = [];

        if (user?.role === 'ADMIN') {
            items.push({
                label: <Link to={adminNavItem.path}>{adminNavItem.icon} {adminNavItem.label}</Link>,
                key: 'admin'
            });
        }

        commonNavItems.forEach((item, index) => {
            items.push({
                label: <Link to={item.path}>{item.icon} {item.label}</Link>,
                key: `nav-${index}`
            });
        });

        items.push({ type: 'divider' });

        items.push({
            label: <Link to={logoutItem.path} onClick={logoutItem.action}>
                {logoutItem.icon} {logoutItem.label}
            </Link>,
            key: 'logout'
        });

        return items;
    }, [user]);

    const AvatarDropdown = useMemo(() => (
        <Dropdown menu={{ items: menuItems }} trigger={['click']}>
            <button className='button-avatar'>
                <Avatar className='avatar' size={40}>
                    {user ? `${user.firstname[0]}${user.lastname[0]}` : 'USER'}
                </Avatar>
            </button>
        </Dropdown>
    ), [menuItems, user]);
  return (
    <header className='navbar'>
    <Link className='navbar__logo' to="/">
        <img src={Isologo} alt="logo" height={40} width={40} />
        <span>Viajemos juntos</span>
    </Link>

    <nav className={`navbar__nav ${menuOpen ? 'navbar__nav--active' : ''}`}>
        {menuOpen &&
            <button onClick={handleClick} className='navbar__toggle-button'>
                <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px">
                    <path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z" />
                </svg>
            </button>
        }

        {user && AvatarDropdown}

        <ul>
            {user ? (
                <>
                    {user.role === 'ADMIN' && (
                        <li className='navbar__link'>
                            <Link to={adminNavItem.path}>
                                {adminNavItem.icon} {adminNavItem.label}
                            </Link>
                        </li>
                    )}
                    {commonNavItems.map(item => (
                        <li key={item.path} className='navbar__link'>
                            <Link to={item.path}>{item.icon} {item.label}</Link>
                        </li>
                    ))}
                    <li className='navbar__link'>
                        <Link to={logoutItem.path} onClick={logoutItem.action}>
                            {logoutItem.icon} {logoutItem.label}
                        </Link>
                    </li>
                </>
            ) : (
                <>
                    <li>
                        <Link to="/registrarse"><UserAddOutlined /> Crear Cuenta</Link>
                    </li>
                    <li>
                        <Link to="/iniciar-sesion"><UserOutlined /> Iniciar Sesión</Link>
                    </li>
                </>
            )}
        </ul>
    </nav>

    {!menuOpen &&
        <button onClick={handleClick} className='navbar__toggle-button'>
            <svg xmlns="http://www.w3.org/2000/svg" height="30px" viewBox="0 -960 960 960" width="30px">
                <path d="M120-240v-80h720v80H120Zm0-200v-80h720v80H120Zm0-200v-80h720v80H120Z" />
            </svg>
        </button>
    }
</header>
    );
};
