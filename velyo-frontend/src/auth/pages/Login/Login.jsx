import React, { useContext, useState } from "react";
import * as Yup from "yup";
import { useAuth } from "../../hook/useAuth";
import { NotificationContext } from "../../../context/NotificationContext";
import { Link, useNavigate } from "react-router-dom";
import { useFormik } from "formik";
import authService from "../../services/auth.service";
import { Loading3QuartersOutlined, UserAddOutlined, UserOutlined } from "@ant-design/icons";
import "./Login.css";
export const Login = () => {
  const [isLoading, setIsLoading] = useState();
  const { toaster } = useContext(NotificationContext);
  const { login } = useAuth();
  const navigate = useNavigate();

  const formik = useFormik({
    initialValues: {
      email: "",
      password: "",
    },
    validationSchema: Yup.object({
      email: Yup.string()
        .trim()
        .email("El email no es valido")
        .required("El email es obligatorio"),
      password: Yup.string()
        .trim()
        .required("La contraseña es obligatiora")
        .min(8, "La contraseña debe tener al menos 8 caracteres"),
    }),
    validateOnChange: true,
    onSubmit: async (values) => {
      try {
        setIsLoading(true);
        const data = await authService.login(values);
        login(data.user, data.token);
        navigate("/");
        toaster["success"]({
          message: "Has iniciado sesion",
          description: "¡Bienvenido a Velyo!",
          duration: 3,
        });
      } catch (error) {
        toaster["error"]({
          message: "Error al iniciar sesión.",
          description: error.message,
          duration: 3,
        });
      } finally {
        setIsLoading(false);
      }
    },
  });
  return (
    <>
      <Link className="button button-base auth-button" to={"/registrarse"}>
        <UserAddOutlined /> Crear Cuenta
      </Link>
      <form className="auth-form" onSubmit={formik.handleSubmit}>
        <h2>Iniciar Sesion</h2>
        <p>Ingresa tus datos para iniciar sesion.</p>
        <div className="form-container">
          <label htmlFor="email" className="form-label form-label-required">
            Email
            <input
              type="email"
              name="email"
              id="email"
              placeholder="Ingrese su mail"
              value={formik.values.email}
              onChange={formik.handleChange}
              autoComplete="email"
              required
            />
          </label>
          {formik.errors.email && (
            <label className="label-error">{formik.errors.email}</label>
          )}
        </div>
        <div className="form-container">
          <label htmlFor="password" className="form-label form-label-required">
            Contraseña
            <input 
            type="password"
            name="password"
            id="password"
            placeholder="Ingrese su contraseña"
            value={formik.values.password}
            onChange={formik.handleChange}
            autoComplete="current-password"
            required
            />
            {formik.errors.password && (
              <label className="label-error">{formik.errors.password}</label>
            )}
          </label>
        </div>
        <div className="form-container">
            <button type="submit" className="button button-primary" disabled={isLoading}>
              {isLoading ?(
                <Loading3QuartersOutlined className="spin-animation"/>
              ):(
                <UserOutlined/>
              )}
              Ingresar
            </button>
            <p className="form-terms">
              <small>No comparta sus datos personales con nadie.</small>
            </p>
        </div>
      </form>
    </>
  );
};
