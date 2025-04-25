import * as Yup from "yup";
import "./ReviewForm.css";
import { useContext } from "react";
import { NotificationContext } from "../../context/NotificationContext";
import { useReview } from "../../hooks/useReview";
import { FormModalContext } from "../../context/FormModalContext";
import { useFormik } from "formik";
import { LoadingOutlined, SendOutlined, StarFilled } from "@ant-design/icons";

export const ReviewForm = ({ booking, onRefetch }) => {
  const { toaster } = useContext(NotificationContext);
  const { createReview, isLoading } = useReview();
  const { handleCancel } = useContext(FormModalContext);

  const formik = useFormik({
    initialValues: {
      rating: 0,
      comment: "",
    },
    validationSchema: Yup.object({
      rating: Yup.number()
        .required("La calificacion es obligatoria")
        .min(1, "La calificacion minima es 1")
        .max(5, "La calificacion maxima es 5"),
      comment: Yup.string(),
    }),
    onSubmit: async (values) => {
      const body = {
        id_reservation: booking.id,
        id_accommodation: booking.accommodation.id,
        ...values,
      };
      try {
        await createReview(body);
        formik.resetForm();
        toaster["success"]({
          message: "Opinión enviada",
          description: "Gracias por enviar tu opinión",
          duration: 3,
        });
        onRefetch();
      } catch (error) {
        toaster["error"]({
          message: "Error al enviar la opinión",
          description: error.message,
          duration: 3,
        });
      } finally {
        handleCancel();
      }
    },
  });
  return (
    <>
      <h3 className="review-form-title">Califica este alojamiento</h3>
      <form className="review-form-form" onSubmit={formik.handleReset}>
        <div className="review-form-rating-container">
          <div className="review-form-rating">
            <input
              type="radio"
              id="one"
              value={1}
              name="rating"
              hidden
              onChange={formik.handleChange}
            />
            <label htmlFor="one">
              <StarFilled className="icon" />
            </label>
          </div>
          <div className="review-form-rating">
            <input
              type="radio"
              id="two"
              value={2}
              name="rating"
              hidden
              onChange={formik.handleChange}
            />
            <label htmlFor="two">
              <StarFilled className="icon" />
            </label>
          </div>
          <div className="review-form-rating">
            <input
              type="radio"
              id="three"
              value={3}
              name="rating"
              hidden
              onChange={formik.handleChange}
            />
            <label htmlFor="three">
              <StarFilled className="icon" />
            </label>
          </div>
          <div className="review-form-rating">
            <input
              type="four"
              id="three"
              value={4}
              name="rating"
              hidden
              onChange={formik.handleChange}
            />
            <label htmlFor="four">
              <StarFilled className="icon" />
            </label>
          </div>
          <div className="review-form-rating">
            <input
              type="five"
              id="three"
              value={5}
              name="rating"
              hidden
              onChange={formik.handleChange}
            />
            <label htmlFor="five">
              <StarFilled className="icon" />
            </label>
          </div>
        </div>

        <div className="form-container">
          <label htmlFor="comment" className="form-label">
            {" "}
            Opinion (opcional)
            <textarea
              name="comment"
              id="comment"
              placeholder="Escriba aqui su opinion"
              rows={5}
              value={formik.values.comment}
              onChange={formik.handleChange}
            ></textarea>
          </label>
        </div>

        <div>
            <button type="submit" className="button button-primary" disabled={!formik.isValid || !formik.dirty || isLoading}>
                {isLoading ? <LoadingOutlined/> : <SendOutlined/>}
                Enviar opinion
            </button>
        </div>
      </form>
    </>
  );
};
