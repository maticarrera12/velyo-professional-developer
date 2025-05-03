import { useContext } from "react";
import "./BookingCard.css";
import { NotificationContext } from "../../context/NotificationContext";
import { FormModalContext } from "../../context/FormModalContext";
import { useBooking } from "../../hooks/useBooking";
import { ReviewForm } from "../ReviewForm/ReviewForm";
import { format } from "date-fns";
import numeral from "numeral";
import {
  CheckCircleOutlined,
  Loading3QuartersOutlined,
} from "@ant-design/icons";

export const BookingCard = ({ booking, onRefetch }) => {
  const { toaster } = useContext(NotificationContext);
  const { handleShowModal, handleContentModal } = useContext(FormModalContext);
  const { confirmBooking, isLoading } = useBooking();

  const handleConfirm = async () => {
    try {
      await confirmBooking(booking.id);
      onRefetch();
      toaster["success"]({
        message: "Reserva confirmada.",
        description: `Su reserva con el código ${booking.id} ha sido confirmada.`,
        duration: 3,
      });
    } catch (error) {
      toaster["error"]({
        message: "Error al confirmar reserva.",
        description: error.message,
        duration: 3,
      });
    }
  };

  const handleShowReviewModal = () => {
    handleContentModal(<ReviewForm booking={booking} onRefetch={onRefetch} />);
    handleShowModal();
  };
  return (
    <article className="booking-card-container">
      <figure className="booking-card-figure">
        {booking.accommodation.avgRating > 0 && (
          <p className="booking-card-avgRating">
            {numeral(booking.accommodation.avgRating).format("0.0")}
          </p>
        )}
        <img
          src={booking.accommodation.images[0]}
          alt={booking.accommodation.name}
        />
        {booking.confirmed &&
          (booking.reviewed ? null : (
            <button
              className="button button-secondary"
              onClick={handleShowReviewModal}
            >
              Calificar alojamiento
            </button>
          ))}
      </figure>

      <section className="booking-card-content">
        <section className="booking-card-accommodation-header">
          <h2>{booking.accommodation.name}</h2>
          <p>
            {booking.accommodation.address.street}{" "}
            {booking.accommodation.address.city},{" "}
            {booking.accommodation.address.country}
          </p>
        </section>

        <section className="booking-card-info">
          <p className="booking-card-info-text">
            Entrada <strong>{format(booking.checkIn, "dd/MM/yyyy")}</strong>
          </p>
          <p className="booking-card-info-text">
            Salida<strong>{format(booking.checkOut, "dd/MM/yyyy")}</strong>
          </p>
          <p className="booking-card-info-text">
            Precio total<strong>${booking.total}</strong>
          </p>
        </section>
          <p className="booking-card-info-text">
            Nombre del cliente{" "}
            <strong>
              {booking.user.firstname} {booking.user.lastname}
            </strong>
          </p>
        <p className="booking-card-info-text">
          Email <strong>{booking.user.email}</strong>
        </p>

        {booking.confirmed ? (
          <p className="booking-card-confirm">
            <CheckCircleOutlined className="icon" /> Confirmada{" "}
          </p>
        ) : (
          <button
            className="button button-primary"
            onClick={handleConfirm}
            disabled={isLoading}
          >
            {isLoading ? (
              <Loading3QuartersOutlined className="spin-animation" />
            ) : (
              <CheckCircleOutlined />
            )}
            Confirmar reserva
          </button>
        )}
      </section>
    </article>
  );
};
