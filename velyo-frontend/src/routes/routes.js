import { Login } from "../auth/pages/Login/Login";
import { Register } from "../auth/pages/Register/Register";
import { AuthLayout } from "../layouts/AuthLayout/AuthLayout";
import { UserLayout } from "../layouts/UserLayout/UserLayout";
import Home from "../pages/Home/Home";

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
    }
]

const routesConfig = [
    ...publicRoutes
]

export default routesConfig


  // {
    //     path:,
    //     component:,
    //     layout:,
    //     roles:,
    //     exact:
    // }