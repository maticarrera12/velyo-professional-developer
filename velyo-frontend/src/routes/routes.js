import { AccommodationList } from "../admin/pages/AccomodationList/AccommodationList";
import { AmenityList } from "../admin/pages/AmenityList/AmenityList";
import { CategoryList } from "../admin/pages/CategoryList/CategoryList";
import { Dashboard } from "../admin/pages/Dashboard/Dashboard";
import { UserList } from "../admin/pages/UserList/UserList";
import { Login } from "../auth/pages/Login/Login";
import { Register } from "../auth/pages/Register/Register";
import { AdminLayout } from "../layouts/AdminLayout/AdminLayout";
import { AuthLayout } from "../layouts/AuthLayout/AuthLayout";
import { UserLayout } from "../layouts/UserLayout/UserLayout";
import Home from "../pages/Home/Home";
import { NotFound } from "../pages/NotFound/NotFound";

const publicRoutes = [
    {
        path:'/',
        component: Home,
        layout: UserLayout,
        roles:[],
        exact: true
    },
  
  {
        path:"/iniciar-sesion",
        component: Login,
        layout: AuthLayout,
        roles: [],
        exact: true
    },
  {
        path:"/registrarse",
        component: Register,
        layout: AuthLayout,
        roles: [],
        exact: true
    },
      {
        path:'*',
        component: NotFound,
        layout: UserLayout,
        roles: [],
        exact: true
    }
]

const adminRoutes = [
    {
        path:'/administracion/dashboard',
        component: Dashboard,
        layout: AdminLayout,
        roles:['ADMIN'],
        exact: true
    },
    {
        path:'/administracion/alojamientos',
        component: AccommodationList,
        layout: AdminLayout,
        roles:['ADMIN'],
        exact: true
    },
    {
        path:'/administracion/categorias',
        component: CategoryList,
        layout: AdminLayout,
        roles:['ADMIN'],
        exact: true
    },
    {
        path:'/administracion/amenities',
        component: AmenityList,
        layout: AdminLayout,
        roles:['ADMIN'],
        exact: true
    },
    {
        path:'/administracion/usuarios',
        component:UserList,
        layout: AdminLayout,
        roles:['ADMIN'],
        exact: true
    },
]
const routesConfig = [
    ...publicRoutes,
    ...adminRoutes
]

export default routesConfig


  // {
    //     path:,
    //     component:,
    //     layout:,
    //     roles:,
    //     exact:
    // }