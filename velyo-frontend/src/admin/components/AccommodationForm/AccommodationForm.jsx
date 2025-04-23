import { Fragment, useContext, useEffect, useRef, useState } from "react";
import * as Yup from "yup";
import { NotificationContext } from "../../../context/NotificationContext";
import { FormModalContext } from "../../../context/FormModalContext";
import { useAccommodation } from "../../../hooks/useAccommodation";
import { useCategory } from "../../../hooks/useCategory";
import { useAmenity } from "../../../hooks/useAmenity";
import { FieldArray, FormikProvider, useFormik } from "formik";
import { useCallback } from "react";
import { useDropzone } from "react-dropzone";
import {
  DropboxOutlined,
  Loading3QuartersOutlined,
  PlusCircleOutlined,
} from "@ant-design/icons";
import { Select } from "antd";
const initialValues = {
  name: "",
  price: 0,
  description: "",
  address: {
    street: "",
    city: "",
    country: "",
  },
  amenities: [],
  category_id: null,
  images: [],
  policies: [{ policy: "", description: "" }],
};

const addressSchema = Yup.object().shape({
  street: Yup.string().required("La calle es obligatoria"),
  city: Yup.string().required("La ciudad es obligatoria"),
  country: Yup.string().required("El país es obligatorio"),
});

const editSchema = Yup.object().shape({
  name: Yup.string().required("El nombre es obligatorio"),
  price: Yup.number()
    .required("El precio es obligatorio")
    .min(1, "El precio debe ser mayor a 0"),
  description: Yup.string().required("La descripción es obligatoria"),
  address: addressSchema,
  category_id: Yup.string().required("La categoría es obligatoria"),
  amenities: Yup.array()
    .min(1, "Se requiere al menos una característica")
    .required("Característica es obligatoria"),
  policies: Yup.array()
    .of(
      Yup.object().shape({
        policy: Yup.string().required(
          "El título de la política es obligatorio"
        ),
        description: Yup.string().required("La descripción es obligatoria"),
      })
    )
    .min(1, "Debes agregar al menos una política"),
});

const createSchema = Yup.object().shape({
  name: Yup.string().required("El nombre es obligatorio"),
  price: Yup.number()
    .required("El precio es obligatorio")
    .min(1, "El precio debe ser mayor a 0"),
  description: Yup.string().required("La descripción es obligatoria"),
  address: addressSchema,
  category_id: Yup.string().required("La categoría es obligatoria"),
  images: Yup.array()
    .min(1, "Se requiere al menos una imagen")
    .required("Imagen es obligatoria"),
  amenities: Yup.array()
    .min(1, "Se requiere al menos una característica")
    .required("Característica es obligatoria"),
  policies: Yup.array()
    .of(
      Yup.object().shape({
        policy: Yup.string().required(
          "El título de la política es obligatorio"
        ),
        description: Yup.string().required("La descripción es obligatoria"),
      })
    )
    .min(1, "Debes agregar al menos una política"),
});
export const AccommodationForm = ({ accommodation, onRefetch }) => {
  const { toaster } = useContext(NotificationContext);
  const { handleCancel } = useContext(FormModalContext);
  const { addAccommodation, editAccommodation, isLoading } = useAccommodation();
  const [files, setFiles] = useState([]);
  const [imagesToDelete, setImagesToDelete] = useState([]);
  const [optionsCategories, setOptionsCategories] = useState([]);
  const [optionsAmenities, setOptionsAmenities] = useState([]);
  const {
    categories,
    getAllCategories,
    isLoading: isLoadingCategories,
    error: errorCategories,
  } = useCategory();
  const {
    amenities,
    getAllAmenities,
    isLoading: isLoadingAmenities,
    error: errorAmenities,
  } = useAmenity();

  useEffect(() => {
    getAllCategories();
    getAllAmenities();
  }, []);

  useEffect(() => {
    if (categories) {
      const options = categories?.map((category) => {
        return {
          label: category.name,
          value: category.id,
        };
      });
      setOptionsCategories(options);
    }
  }, [categories]);
  useEffect(() => {
    if (amenities) {
      const options = amenities?.map((amenity) => {
        return {
          label: amenity.name,
          value: amenity.id,
        };
      });
      setOptionsAmenities(options);
    }
  }, [amenities]);

  const formik = useFormik({
    initialValues: initialValues,
    validationSchema: accommodation ? editSchema : createSchema,
    validateOnChange: true,
    onSubmit: async (values) => {
      if (accommodation) {
        if (
          imagesToDelete.length === accommodation.images.length &&
          !formik.values.images
        ) {
          alert("No se pueden eliminar todas las imagenes y no agregar nuevas");
        } else {
          try {
            values.id = accommodation.id;
            values.imagesToDelete = imagesToDelete;
            await editAccommodation(values);
            formik.resetForm();
            setFiles([]);
            setImagesToDelete([]);
            handleCancel();
            onRefetch();
            toaster["success"]({
              message: "Alojamiento editado correctamente",
              description: "El alojamiento fue editado correctamente.",
              duration: 3,
            });
          } catch (error) {
            toaster["error"]({
              message: "Error al editar el alojamiento",
              description: error.message,
              duration: 3,
            });
          }
        }
      } else {
        try {
          await addAccommodation(values);
          formik.resetForm();
          setFiles([]);
          handleCancel();
          onRefetch();
          toaster["success"]({
            message: "Alojamiento creado correctamente",
            description: "El alojamiento fue creado correctamente.",
            duration: 3,
          });
        } catch (error) {
          toaster["error"]({
            message: "Error al crear el alojamiento",
            description: error.message,
            duration: 3,
          });
        }
      }
    },
  });

  const handleImageChange = (event, imageUrl) => {
    const isChecked = event.target.checked;
    const imageName = imageUrl.split("/").pop();
    setImagesToDelete((prevState) => {
      if (isChecked) {
        return [...prevState, imageName];
      } else {
        return prevState.filter((image) => image !== imageName);
      }
    });
  };

  const checkboxesRef = useRef([]);

  useEffect(() => {
    if (accommodation) {
      formik.setValues({
        name: accommodation.name,
        price: accommodation.price,
        description: accommodation.description,
        address: accommodation.address,
        category_id: accommodation.category_id,
        amenities: accommodation.amenities.map((amenity) => amenity.id),
        policies: accommodation.policies,
      });
      setImagesToDelete([]);
      checkboxesRef.current.forEach((checkbox) => {
        if (checkbox) checkbox.checked = false;
      });
    } else {
      formik.resetForm();
      setFiles([]);
    }
  }, [accommodation]);

  useEffect(() => {
    return () => files.forEach((file) => URL.revokeObjectURL(file.preview));
  }, [files]);

  const onDrop = useCallback((acceptedFiles) => {
    formik.setFieldValue("images", acceptedFiles);
    setFiles(
      acceptedFiles.map((file) =>
        Object.assign(file, {
          preview: URL.createObjectURL(file),
        })
      )
    );
  }, []);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    accept: {
      "image/*": [],
    },
    onDrop,
    maxFiles: 10,
    noClick: false,
    noKeyboard: false,
  });

  const thumbs = files.map((file) => (
    <div className="thumb" key={file.name}>
      <figure className="thumb-inner">
        <img
          src={file.preview}
          title={file.name}
          className="thumb-img"
          onLoad={() => {
            URL.revokeObjectURL(file.preview);
          }}
          alt="Preview"
        />
      </figure>
    </div>
  ));

  return (
    <>
      <h2 className="item-form-title">
        {accommodation ? "Editar" : "Agregar"}
      </h2>
      <form
        className="item-form-form item-form-form-overflow"
        onSubmit={formik.handleSubmit}
      >
        <div className="form-col-2">
          <div className="form-container">
            <label className="form-label">
              Nombre
              <input
                type="text"
                placeholder="Nombre"
                name="name"
                value={formik.values.name}
                onChange={formik.handleChange}
                required
              />
            </label>
            {formik.errors.name && formik.touched.name && (
              <label className="label-error">{formik.errors.name}</label>
            )}
          </div>
          <div className="form-container">
            <label className="form-label">
              Precio
              <input
                type="number"
                min={1}
                placeholder="Precio"
                name="price"
                value={formik.values.price}
                onChange={formik.handleChange}
                required
              />
            </label>
            {formik.errors.price && formik.touched.price && (
              <label className="label-error">{formik.errors.price}</label>
            )}
          </div>
        </div>
        <div className="form-container">
          <label className="form-label">
            Categoría
            <Select
              id="category_id"
              name="category_id"
              className="select"
              value={formik.values.category_id}
              onChange={(value) => formik.setFieldValue("category_id", value)}
              options={optionsCategories}
              placeholder="Seleccióne una categoria"
              optionFilterProp="label"
              disabled={isLoadingCategories || errorCategories}
            />
          </label>
          {formik.errors.category_id && formik.touched.category_id && (
            <label className="label-error">{formik.errors.category_id}</label>
          )}
        </div>
        <div className="form-container">
          <label className="form-label">
            Características
            <Select
              id="amenities"
              name="amenities"
              className="select"
              mode="multiple"
              allowClear
              value={formik.values.amenities}
              onChange={(value) => formik.setFieldValue("amenities", value)}
              options={optionsAmenities}
              placeholder="Seleccióne las características"
              optionFilterProp="label"
              disabled={isLoadingAmenities || errorAmenities}
            />
          </label>
          {formik.errors.amenities && formik.touched.amenities && (
            <label className="label-error">{formik.errors.amenities}</label>
          )}
        </div>
        <div className="form-container">
          <label className="form-label">
            Calle
            <input
              type="text"
              placeholder="Calle"
              name="address.street"
              value={formik.values.address.street}
              onChange={formik.handleChange}
            />
          </label>
          {formik.errors.address?.street && formik.touched.address?.street && (
            <label className="label-error">
              {formik.errors.address.street}
            </label>
          )}
        </div>

        <div className="form-col-2">
          <div className="form-container">
            <label className="form-label">
              País
              <input
                type="text"
                placeholder="País"
                name="address.country"
                value={formik.values.address.country}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
              />
            </label>
            {formik.errors.address?.country &&
              formik.touched.address?.country && (
                <label className="label-error">
                  {formik.errors.address.country}
                </label>
              )}
          </div>
          <div className="form-container">
            <label className="form-label">
              Provincia o Ciudad
              <input
                type="text"
                placeholder="Provincia o Ciudad"
                name="address.city"
                value={formik.values.address.city}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
              />
            </label>
            {formik.errors.address?.city && formik.touched.address?.city && (
              <label className="label-error">
                {formik.errors.address.city}
              </label>
            )}
          </div>
        </div>
        <div className="form-container">
          <label className="form-label">
            Descripción
            <textarea
              placeholder="Descripción"
              name="description"
              value={formik.values.description}
              onChange={formik.handleChange}
            />
          </label>
          {formik.errors.description && formik.touched.description && (
            <label className="label-error">{formik.errors.description}</label>
          )}
        </div>
        {accommodation && (
          <div className="form-container">
            <label className="form-label">
              Imagenes actuales (Selecciona las imagenes a eliminar)
            </label>
            <div className="form-image-container">
              {accommodation.images.map((image, index) => (
                <div key={index} className="form-image">
                  <label htmlFor={`imagesToDelete[${index}]`}>
                    <img src={image} alt={accommodation.name} />
                  </label>
                  <input
                    type="checkbox"
                    id={`imagesToDelete[${index}]`}
                    name={`imagesToDelete[${index}]`}
                    value={image}
                    ref={(el) => (checkboxesRef.current[index] = el)}
                    onChange={(e) => handleImageChange(e, image)}
                  />
                </div>
              ))}
            </div>
          </div>
        )}
        <div className="form-container">
          <label className="form-label">
            Imágenes
            <div
              {...getRootProps({
                className: `dropzone ${isDragActive ? "dropzone-active" : ""}`,
              })}
              onClick={(e) => e.stopPropagation()}
            >
              <input {...getInputProps()} />
              <DropboxOutlined
                style={{ fontSize: "24px", marginBottom: "8px" }}
              />
              <p>
                {isDragActive
                  ? "¡Suelta los archivos aquí!"
                  : "Arrastra y suelta algunas imágenes aquí, o haz clic para seleccionar imágenes"}
              </p>
            </div>
          </label>
          {formik.errors.images && formik.touched.images && (
            <label className="label-error">{formik.errors.images}</label>
          )}
          <aside className="thumbsContainer">{thumbs}</aside>
        </div>
        <FormikProvider value={formik}>
          <FieldArray name="policies">
            {({ push, remove }) => (
              <>
                <button
                  type="button"
                  className="button button-base"
                  style={{ alignSelf: "start" }}
                  onClick={() => push({ policy: "", description: "" })}
                >
                  <PlusCircleOutlined />
                  Añadir política
                </button>
                {formik.values.policies.map((_, index) => (
                  <Fragment key={index}>
                    <div className="form-container">
                      <label className="form-label">
                        Título de la política
                        <input
                          type="text"
                          name={`policies[${index}].policy`}
                          value={formik.values.policies[index].policy}
                          onChange={formik.handleChange}
                        />
                      </label>
                      {formik.errors.policies?.[index]?.policy &&
                        formik.touched.policies?.[index]?.policy && (
                          <div style={{ color: "red" }}>
                            {formik.errors.policies[index].policy}
                          </div>
                        )}
                    </div>
                    <div className="form-container">
                      <label className="form-label">
                        Descripción
                        <textarea
                          name={`policies[${index}].description`}
                          value={formik.values.policies[index].description}
                          onChange={formik.handleChange}
                        />
                      </label>

                      {formik.errors.policies?.[index]?.description &&
                        formik.touched.policies?.[index]?.description && (
                          <div style={{ color: "red" }}>
                            {formik.errors.policies[index].description}
                          </div>
                        )}
                    </div>
                    {formik.values.policies.length > 1 && (
                      <button
                        type="button"
                        className="button button-danger"
                        onClick={() => remove(index)}
                        style={{ alignSelf: "end" }}
                      >
                        <DeleteFilled />
                        Eliminar política
                      </button>
                    )}
                  </Fragment>
                ))}
              </>
            )}
          </FieldArray>
        </FormikProvider>
        <button
          type="submit"
          className="button button-primary"
          disabled={isLoading}
        >
          {isLoading ? (
            <Loading3QuartersOutlined className="spin-animation" />
          ) : (
            <PlusCircleOutlined />
          )}
          {accommodation ? "Editar" : "Agregar"}
        </button>
      </form>
    </>
  );
};
