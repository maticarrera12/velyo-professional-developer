import React, { useContext, useState } from "react";
import * as Yup from "yup";
import { NotificationContext } from "../../../context/NotificationContext";
import { Link, useNavigate } from "react-router-dom";
import { useFormik } from "formik";
import authService from "../../services/auth.service";
import { Loading3QuartersOutlined, UserOutlined } from "@ant-design/icons";
export const Register = () => {
  const { toaster } = useContext(NotificationContext);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const formik = useFormik({
    initialValues: {
      firstname: "",
      lastname: "",
      email: "",
      password: "",
    },
    validationSchema: Yup.object({
      firstname: Yup.string().trim().required("El nombre es obligatorio"),
      lastname: Yup.string().trim().required("El apellido es obligatorio"),
      email: Yup.string()
        .trim()
        .email("El email no es valido")
        .required("El email es obligatorio"),
      password: Yup.string()
        .trim()
        .required("La contraseña es obligatoria")
        .min(8, "La contraseña debe tener al menos 8 caracteres"),
    }),
    validateOnChange: true,
    onSubmit: async (newUser) => {
      try {
        setIsLoading(true);
        await authService.register(newUser);
        toaster["success"]({
          message: "Registro exitoso",
          description: "¡Bienvenido a velyo!",
          duration: 3,
        });
        navigate("/iniciar-sesion");
      } catch (error) {
        toaster["error"]({
          message: "Ha ocurrido un error en el registro",
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
      <Link className="button button-base auth-button" to={"/iniciar-sesion"}>
        <UserOutlined />
        Iniciar Sesion
      </Link>
      <form className="auth-form" onSubmit={formik.handleSubmit}>
        <h2>Crear cuenta</h2>
        <p>Ingresa tus datos abajo para registrarte</p>
        <div className="form-container">
          <label htmlFor="firstname" className="form-label form-label-required">
            Nombre
            <input
              type="text"
              name="firstname"
              id="firstname"
              placeholder="Ingrese su nombre"
              value={formik.values.firstname}
              onChange={formik.handleChange}
              required
            />
          </label>
          {formik.errors.firstname && (
            <label className="label-error">{formik.errors.firstname}</label>
          )}
        </div>
        <div className="form-container">
          <label htmlFor="lastname" className="form-label form-label-required">
            Apellido
            <input
              type="text"
              name="lastname"
              id="lastname"
              placeholder="Ingrese su apellido"
              value={formik.values.lastname}
              onChange={formik.handleChange}
              required
            />
          </label>
          {formik.errors.lastname && (
            <label className="label-error">{formik.errors.lastname}</label>
          )}
        </div>
        <div className="form-container">
          <label htmlFor="email" className="form-label form-label-required">
            Email
            <input
              type="email"
              name="email"
              id="email"
              placeholder="Ingrese su email"
              value={formik.values.email}
              onChange={formik.handleChange}
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
              placeholder="Ingrese su Contraseña"
              value={formik.values.password}
              onChange={formik.handleChange}
              required
            />
          </label>
          {formik.errors.password && (
            <label className="label-error">{formik.errors.password}</label>
          )}
        </div>
        <div className="form-container">
          <button
            type="submit"
            className="button button-primary"
            disabled={isLoading}
          >
            {
              isLoading ? (
                <Loading3QuartersOutlined className="spin-animation"/>
              ):(
                <UserOutlined/>
              )}
              Registrarme
          </button>
        </div>
        <p className="form-data">
          <small>
            Al registrarte aceptas los terminos y condiciones de velyo
          </small>
        </p>
      </form>
    </>
  );
};
