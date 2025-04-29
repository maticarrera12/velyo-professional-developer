import api from "../api";

const bookingService = {
  createBooking: async (body, token) => {
    return api
      .post("/bookings", body, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      })
      .catch(() => {
        throw new Error(
          "No se pudo crear la reserva. Por favor, intente nuevamente."
        );
      });
  },
  getBookingsByUser: async (token, date) => {
    const params = date ? `?date=${date}` : "";
    return api
      .get(`/bookings/user${params}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      })
      .catch(() => {
        throw new Error(
          "No se pudieron obtener las reservas. Por favor, intente nuevamente."
        );
      });
  },

  confirmBooking: async (id, token) => {
    return api
      .put(
        `/bookings/confirm/${id}`,
        {},
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      )
      .catch((e) => {
        throw new Error(e.response.data);
      });
  },
};
export default bookingService;
