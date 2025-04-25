import { Link, useNavigate, useParams } from "react-router-dom"
import "./BookingConfirm.css"
import { useContext } from "react"
import { NotificationContext } from "../../context/NotificationContext"
import { useBooking } from "../../hooks/useBooking"
import { CheckCircleOutlined, Loading3QuartersOutlined, SmileOutlined } from "@ant-design/icons"

export const BookingConfirm = () => {

  const navigate = useNavigate()
  const {toaster} = useContext(NotificationContext)
  const { id } = useParams()
  const {confirmBooking, isLoading} = useBooking()

  const handleConfirm = async () => {
    try {
      await confirmBooking(id)
      toaster['success']({
        message: 'Reserva confirmada.',
        description: `Su reserva con el código ${id} ha sido confirmada.`,
        duration: 3
    });
    navigate('/mis-reservas');
    } catch (error) {
      toaster['error']({
        message: 'Error al confirmar reserva.',
        description: error.message,
        duration: 3
    });
    }
  }
  return (
    <main className="booking-confirm-container">
      <section>
        <SmileOutlined className="booking-confirm-icon"/>
        <h2 className="booking-confirm-title">
        ¡Felicidades su reserva se ha realizado con éxito!
        </h2>
        <p>
        No te olvides confirmar tu reserva, podes hacerlo ahora mismo o más adelante.
        </p>
      </section>
      <section className="booking-confirm-action">
        <button className="button button-primary" onClick={handleConfirm} disabled={isLoading}>
          {
            isLoading ? <Loading3QuartersOutlined className="spin-animation"/> : <CheckCircleOutlined/>
          }
          Confirmar reserva
        </button>
        <Link to='/mis-reservas' className="button button-secondary">
        Ir a Reservas</Link>
      </section>
    </main>
  )
}
