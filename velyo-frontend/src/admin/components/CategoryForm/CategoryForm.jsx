import { useContext, useEffect, useState } from "react";
import * as Yup from "yup";
import { NotificationContext } from "../../../context/NotificationContext";
import { useCategory } from "../../../hooks/useCategory";
import { FormModalContext } from "../../../context/FormModalContext";
import { useFormik } from "formik";
import { useDropzone } from "react-dropzone";
import { DropboxOutlined, Loading3QuartersOutlined, PlusCircleOutlined } from "@ant-design/icons";

export const CategoryForm = ({ category, onRefetch }) => {
  const { toaster } = useContext(NotificationContext);
  const { isLoading, addCategory, editCategory } = useCategory();
  const { handleCancel } = useContext(FormModalContext);
  const [files, setFiles] = useState([]);

  const formik = useFormik({
    initialValues: {
      name: "",
      description: "",
      image: [],
    },
    validationSchema: Yup.object(
      category
        ? {
            name: Yup.string().required("El nombre es obligatorio"),
            description: Yup.string().required("La descripcion es obligatoria"),
          }
        : {
            name: Yup.string().required("El nombre es obligatorio"),
            description: Yup.string().required("La descripcion es obligatoria"),
            image: Yup.array()
              .min(1, "Se requiere una imagen")
              .required("La imagen es obligatoria"),
          }
    ),
    validateOnChange: true,
    onSubmit: async (values) => {
      if (category) {
        try {
          values.id = category.id;
          await editCategory(values);
          formik.resetForm();
          setFiles([]);
          handleCancel();
          onRefetch();
          toaster["success"]({
            message: "Categoría editada correctamente",
            description: "La categoría se ha editado correctamente",
            duration: 3,
          });
        } catch (error) {
          toaster["error"]({
            message: "Error al editar la categoría",
            description: error.message,
            duration: 3,
          });
        }
      } else {
        try {
          await addCategory(values);
          formik.resetForm();
          setFiles([]);
          handleCancel();
          onRefetch();
          toaster["success"]({
            message: "Categoría agregada correctamente",
            description: "La categoría se ha agregado correctamente",
            duration: 3,
          });
        } catch (error) {
          toaster["error"]({
            message: "Error al agregar la categoría",
            description: error.message,
            duration: 3,
          });
        }
      }
    },
  });

  useEffect(() => {
    if (category) {
      formik.setValues({
        name: category.name,
        description: category.description,
      });
    } else {
      formik.resetForm();
      setFiles([]);
    }
  }, [category]);

  useEffect(() => {
    return () => files.forEach((file) => URL.revokeObjectURL(file.preview));
  }, [files]);

  const { getRootProps, getInputProps } = useDropzone({
    accept: {
      "image/*": [],
    },
    onDrop: (acceptedFiles) => {
      formik.setFieldValue("image", acceptedFiles);
      setFiles(
        acceptedFiles.map((file) =>
          Object.assign(file, {
            preview: URL.createObjectURL(file),
          })
        )
      );
    },
    maxFiles: 1,
  });

  const thumbs = files.map(file => (
    <div className="thumb" key={file.name}>
      <figure className="thumb-inner">
        <img
          src={file.preview}
          alt={file.name}
          className="thumb-img"
          onLoad={() => {
            URL.revokeObjectURL(file.preview);
          }}
        />
      </figure>
    </div>
  ));
  return (
    <>
      <h2 className="item-form-title">{category ? "Editar" : "Agregar"}</h2>

      <form className="item-form-form" onSubmit={formik.handleSubmit}>
        <div className="form-container">
          <label className="form-label">
            Nombre
            <input
              type="text"
              placeholder="Nombre"
              name="name"
              value={formik.values.name}
              onChange={formik.handleChange}
            />
          </label>
          {formik.errors.name && (
            <label className="label-error">{formik.errors.name}</label>
          )}
        </div>
        <div className="form-container">
          <label className="form-label">
            Descripcion
            <input
              type="text"
              placeholder="Descripcion"
              name="description"
              value={formik.values.description}
              onChange={formik.handleChange}
            />
          </label>
          {formik.errors.description && (
            <label className="label-error">{formik.errors.description}</label>
          )}
        </div>
        {category && (
          <div className="form-container">
            <label className="form-label">
              Imagen
              <figure className="form-image">
                <img src={category.image} alt={category.name} />
              </figure>
            </label>
          </div>
        )}

        <div className="form-container">
          <label className="form-label">
            {category ? "Nueva Imagen" : "Imagen"}
            <div {...getRootProps({ className: "dropzone" })}>
              <input {...getInputProps()} />
              <DropboxOutlined />
              <p>Arrastra una imagen o haz click para seleccionar una</p>
            </div>
          </label>
          {formik.errors.image && (
            <label className="label-error">{formik.errors.image}</label>
          )}
          <aside className="thumb-container">{thumbs}</aside>
        </div>

        <button type="submit" className="button button-primary" disabled={isLoading}>
            {
                isLoading ? <Loading3QuartersOutlined className="spin-animation"/> :
                <PlusCircleOutlined />
            }
            {category ? "Editar" : "Agregar"}
        </button>
      </form>
    </>
  );
};
