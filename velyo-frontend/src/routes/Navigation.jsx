import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { QueryParamProvider } from 'use-query-params';
import { ReactRouter6Adapter } from 'use-query-params/adapters/react-router-6';
import routesConfig from './routes';
import { PrivateRoute } from './PrivateRoute';  // Asegúrate de que PrivateRoute esté bien implementado
import { PublicRoute } from './PublicRoute';

export const Navigation = () => {
  return (
    <BrowserRouter>
      <QueryParamProvider adapter={ReactRouter6Adapter}>
        <Routes>
          {routesConfig.map(({ path, component: Component, layout: Layout, roles }, index) => (
            <Route
              key={index}
              path={path}
              element={
                roles.length > 0 ? (
                  <PrivateRoute roles={roles}>
                    <Layout>
                      <Component />
                    </Layout>
                  </PrivateRoute>
                ) : path === '/iniciar-sesion' || path === '/registrarse' ? (
                  <PublicRoute>
                    <Layout>
                      <Component />
                    </Layout>
                  </PublicRoute>
                ) : (
                  <Layout>
                    <Component />
                  </Layout>
                )
              }
            />
          ))}
        </Routes>
      </QueryParamProvider>
    </BrowserRouter>
  );
};


