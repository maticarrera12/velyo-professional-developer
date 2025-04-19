import { useFormik } from "formik";
import { useContext, useEffect } from "react";
import * as Yup from "yup";
import { FormModalContext } from "../../../context/FormModalContext";
import { NotificationContext } from "../../../context/NotificationContext";
import { useUser } from "../../../hooks/useUser";
import { Select } from "antd";
import { Loading3QuartersOutlined, PlusCircleOutlined } from "@ant-design/icons";

export const UserForm = ({ user, onRefetch }) => {
  const { toaster } = useContext(NotificationContext);
  const { handleCancel } = useContext(FormModalContext);
  const { addUser, editUser, isLoading } = useUser();

  const formik = useFormik({
    initialValues: {
      firstname: "",
      lastname: "",
      email: "",
      role: "USER",
      password: "",
    },
    validationSchema: Yup.object(
        user
          ? {
              firstname: Yup.string().required("El nombre es obligatorio"),
              lastname: Yup.string().required("El apellido es obligatorio"),
              email: Yup.string()
                .required("El email es obligatorio")
                .email("El email no es válido"),
              role: Yup.string().required("El rol es obligatorio"),
              password: Yup.string().min(
                8,
                "La contraseña debe tener al menos 8 caracteres"
              ),
            }
          : {
              firstname: Yup.string().required("El nombre es obligatorio"),
              lastname: Yup.string().required("El apellido es obligatorio"),
              email: Yup.string()
                .required("El email es obligatorio")
                .email("El email no es válido"),
              role: Yup.string().required("El rol es obligatorio"),
              password: Yup.string()
                .required("La contraseña es obligatoria")
                .min(8, "La contraseña debe tener al menos 8 caracteres"),
            }
      ),
    validateOnChange: true,
    onSubmit: async (values) => {
      if (user) {
        try {
          values.id = user.id;
          await editUser(values);
          formik.resetForm();
          handleCancel(),
            onRefetch(),
            toaster["success"]({
              message: "Usuario editado correctamente",
              description: "El usuario se ha editado correctamente",
              duration: 3,
            });
        } catch (error) {
          toaster["error"]({
            message: "Error al editar el usuario",
            description: error.message,
            duration: 3,
          });
        }
      } else {
        try {
          await addUser(values);
          formik.resetForm();
          handleCancel();
          onRefetch();
          toaster["success"]({
            message: "Usuario agregado correctamente",
            description: "El usuario se ha agregado correctamente",
            duration: 3,
          });
        } catch (error) {
          toaster["error"]({
            message: "Error al agregar el usuario",
            description: error.message,
            duration: 3,
          });
        }
      }
    },
  });

  useEffect(() => {
    if (user) {
      formik.setValues({
        firstname: user.firstname,
        lastname: user.lastname,
        email: user.email,
        role: user.role,
      });
    } else {
      formik.resetForm();
    }
  }, [user]);
  return (
    <>
      <h2 className="item-form-title">{user ? "Editar" : "Agregar"}</h2>
      <form className="item-form-form" onSubmit={formik.handleSubmit}>
        <div className="form-container">
          <label htmlFor="firstname" className="form-label">
            Nombre
            <input
              type="text"
              name="firstname"
              id="firstname"
              value={formik.values.firstname}
              onChange={formik.handleChange}
            />
          </label>
          {formik.errors.firstname && (
            <label className="label-error">{formik.errors.firstname}</label>
          )}
        </div>
        <div className="form-container">
          <label htmlFor="lastname" className="form-label">
            Apellido
            <input
              type="text"
              name="lastname"
              id="lastname"
              value={formik.values.lastname}
              onChange={formik.handleChange}
            />
          </label>
          {formik.errors.lastname && (
            <label className="label-error">{formik.errors.lastname}</label>
          )}
        </div>
        <div className="form-container">
          <label htmlFor="email" className="form-label">
            Email
            <input
              type="email"
              name="email"
              id="email"
              value={formik.values.email}
              onChange={formik.handleChange}
            />
          </label>
          {formik.errors.email && (
            <label className="label-error">{formik.errors.email}</label>
          )}
        </div>
        <div className="form-container">
          <label htmlFor="role" className="form-label">
            Rol
            <Select
              id="role"
              name="role"
              className="select"
              value={formik.values.role}
              onChange={(value) => formik.setFieldValue("role", value)}
              options={[
                { value: "ADMIN", label: "Administrador" },
                { value: "USER", label: "Usuario" },
              ]}
              placeholder="Seleccionar un Rol"
              optionFilterProp="label"
            />
          </label>
          {formik.errors.role && (
            <label className="label-error">{formik.errors.role}</label>
          )}
        </div>
        <div className="form-container">
          <label htmlFor="password" className="form-label">
            {user ? "Nueva contraseña" : "Contraseña"}
            <input
              type="password"
              name="password"
              id="password"
              value={formik.values.password}
              onChange={formik.handleChange}
            />
          </label>
          {formik.errors.password && (
            <label className="label-error">{formik.errors.password}</label>
          )}
        </div>

        <button type="submit" className="button button-primary" disabled={isLoading}>
            {isLoading ? (
                <Loading3QuartersOutlined className="spin-animation"/>
            ):(
                <PlusCircleOutlined/>
            )}
            {user? "Editar" : "Agregar"}
        </button>
      </form>
    </>
  );
};
