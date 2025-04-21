import { useCallback, useContext, useEffect, useState } from "react";
import * as Yup from "yup";
import { NotificationContext } from "../../../context/NotificationContext";
import { useAmenity } from "../../../hooks/useAmenity";
import { FormModalContext } from "../../../context/FormModalContext";
import { useFormik } from "formik";
import { useDropzone } from "react-dropzone";
import {
    DropboxOutlined,
  Loading3QuartersOutlined,
  PlusCircleOutlined,
} from "@ant-design/icons";
export const AmenityForm = ({ amenity, onRefetch }) => {
  const { toaster } = useContext(NotificationContext);
  const { isLoading, addAmenity, editAmenity } = useAmenity();
  const { handleCancel } = useContext(FormModalContext);
  const [files, setFiles] = useState([]);

  const formik = useFormik({
    initialValues: {
      name: "",
      icon: [],
    },
    validationSchema: Yup.object(
      amenity
        ? {
            name: Yup.string().required("El nombre es obligatorio"),
          }
        : {
            name: Yup.string().required("El nombre es obligatorio"),
            icon: Yup.array()
              .min(1, "Se rquiere un icono")
              .required("El icono es obligatorio"),
          }
    ),
    validateOnChange: true,
    onSubmit: async (values) => {
      if (amenity) {
        try {
          (values.id = amenity.id), await editAmenity(values);
          formik.resetForm();
          setFiles([]);
          handleCancel();
          onRefetch();
          toaster["success"]({
            message: "Característica editada correctamente",
            description: "La característica se ha editado correctamente",
            duration: 3,
          });
        } catch (error) {
          toaster["error"]({
            message: "Error al editar la característica",
            description: error.message,
            duration: 3,
          });
        }
      } else {
        try {
          await addAmenity(values);
          formik.resetForm(), setFiles([]);
          handleCancel();
          onRefetch();
          toaster["success"]({
            message: "Característica agregada correctamente",
            description: "La característica se ha agregado correctamente",
            duration: 3,
          });
        } catch (error) {
          toaster["error"]({
            message: "Error al agregar la característica",
            description: error.message,
            duration: 3,
          });
        }
      }
    },
  });

  useEffect(() => {
    if (amenity) {
      formik.setValues({
        name: amenity.name,
      });
    } else {
      formik.resetForm();
      setFiles([]);
    }
  }, [amenity]);

  useEffect(() => {
    return () => {
      files.forEach((file) => URL.revokeObjectURL(file.preview));
    };
  }, [files]);

  const onDrop = useCallback((acceptedFiles) => {
    const svgFiles = acceptedFiles.filter(
      (file) => file.type === "image/svg+xml" || file.name.endsWith("svg")
    );

    if (svgFiles.length === 0) {
      toaster["error"]({
        message: "Formato incorrecto",
        description: "Solo se permiten archivos SVG",
        duration: 3,
      });
      return;
    }
    formik.setFieldValue("icon", svgFiles);
    setFiles(
      svgFiles.map((file) =>
        Object.assign(file, {
          preview: URL.createObjectURL(file),
        })
      )
    );
  }, []);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    accept: {
      "image/svg+xml": [".svg"],
    },
    onDrop,
    maxfiles: 1,
    noClick: false,
    noKeyboard: false,
    multiple: false,
  });

  const thumbs = files.map((file) => (
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
      <h2 className="item-form-title">{amenity ? "Editar" : "Agregar"}</h2>
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

        {amenity && (
          <div className="form-container">
            <label className="form-label">
              Icono Actual
              <figure className="form-image">
                <img src={amenity.icon} alt={amenity.name} />
              </figure>
            </label>
          </div>
        )}

        <div className="form-container">
          <label className="form-label">
            {amenity ? "Nuevo Icono" : "Icono"}
            <div
              {...getRootProps({
                className: `dropzone ${isDragActive ? "dropzone--active" : ""}`,
              })}
              onClick={(e) => e.stopPropagation()}
            >
              <input {...getInputProps()} />
              <DropboxOutlined
                style={{ fontSize: "24px", marginBottom: "8px" }}
              />
              <p>
                {isDragActive
                  ? "Suelta el archivo SVG aquí"
                  : "Arrastra y suelta un icono SVG aquí, o haz clic para seleccionar"}
              </p>
            </div>
          </label>
          {formik.errors.icon && (
            <label htmlFor="" className="label-error">
              {formik.errors.icon}
            </label>
          )}
          <aside className="thumb-container">{thumbs}</aside>
        </div>

        <button
          className="button button-primary"
          type="submit"
          disabled={isLoading}
        >
          {isLoading ? (
            <Loading3QuartersOutlined className="spin-animation" />
          ) : (
            <PlusCircleOutlined />
          )}
          {amenity ? "Editar" : "Agregar"}{" "}
        </button>
      </form>
    </>
  );
};
