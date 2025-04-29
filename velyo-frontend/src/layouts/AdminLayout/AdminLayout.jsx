import React from 'react';
import "./AdminLayout.css";
import { Link, NavLink } from 'react-router-dom';
import Isologo from "../../assets/images/velyo_logotipo.svg";
import { AppstoreOutlined, HomeFilled, HomeOutlined, TagsOutlined, UserOutlined } from '@ant-design/icons';
import { FormModalProvider } from '../../context/FormModalProvider';
import { LuLayoutDashboard } from 'react-icons/lu';

const routes = [
  {
    path: "/administracion/dashboard",
    name: "Dashboard",
    icon: <LuLayoutDashboard />,
  },
  {
    path: "/administracion/alojamientos",
    name: "Alojamientos",
    icon: <HomeOutlined />,
  },
  {
    path: "/administracion/usuarios",
    name: "Usuarios",
    icon: <UserOutlined />,
  },
  {
    path: "/administracion/categorias",
    name: "Categorías",
    icon: <TagsOutlined />,
  },
  {
    path: "/administracion/amenities",
    name: "Amenities",
    icon: <AppstoreOutlined />,
  },
];

export const AdminLayout = ({ children }) => {
  return (
    <main>
      <div className="admin-layout-container">
        <section className='sidebar-container'>
          <nav className='sidebar-nav'>
            <Link to={"/administracion/dashboard"}>
              <img src={Isologo} alt="Velyo" />
            </Link>

            <ul className='sidebar-list'>
              <p className='sidebar-title'>Administración</p>
              {routes.map((route, index) => (
                <li key={index}>
                  <NavLink
                    to={route.path}
                    className={({ isActive }) =>
                      isActive ? 'sidebar-item sidebar-item-active' : 'sidebar-item'
                    }>
                    <span className='sidebar-icon'>{route.icon}</span>
                    {route.name}
                  </NavLink>
                </li>
              ))}
            </ul>

            <div className="sidebar-footer">
              <Link to="/" className='button button-primary'>
                <HomeFilled /> Volver al inicio
              </Link>
            </div>
          </nav>
        </section>

        <FormModalProvider>
          {children}
        </FormModalProvider>
      </div>

      <h1 className='admin-layout-display-message'>
        Debe ingresar desde una computadora o dispositivo con resolución mayor a 1024px.
      </h1>
    </main>
  );
};
