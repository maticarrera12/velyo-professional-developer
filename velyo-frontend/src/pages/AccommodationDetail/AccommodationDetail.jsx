import { useContext, useEffect, useRef, useState } from "react";
import "./AccommodationDetail.css";
import { NotificationContext } from "../../context/NotificationContext";
import locale from "antd/es/date-picker/locale/es_ES";
import { useAuth } from "../../auth/hook/useAuth";
import { DatePicker, Empty, Skeleton } from "antd";
import { Carousel } from "react-responsive-carousel";
import { useAccommodation } from "../../hooks/useAccommodation";
import { FormModalContext } from "../../context/FormModalContext";
import { Link, useNavigate, useParams } from "react-router-dom";
import userService from "../../services/users.service";
import { differenceInDays } from "date-fns";
import { useBooking } from "../../hooks/useBooking";
import { AccommodationShare } from "../../components/AccommodationShare/AccommodationShare";
import ImageFallback from "../../assets/images/image-fallback.jpg";
import "react-responsive-carousel/lib/styles/carousel.min.css";
import dayjs from "dayjs";
import {
  ArrowLeftOutlined,
  CalendarOutlined,
  CameraOutlined,
  HeartFilled,
  HeartOutlined,
  Loading3QuartersOutlined,
  ShareAltOutlined,
  StarFilled,
  UserOutlined,
} from "@ant-design/icons";
import { Modal } from "../../ui/Modal/Modal";
import { ReactSVG } from "react-svg";
import numeral from "numeral";
import { ReviewCard } from "../../components/ReviewCard/ReviewCard";
export const AccommodationDetail = () => {
  const { toaster } = useContext(NotificationContext);
  const [isLoadingFavorite, setIsLoadingFavorite] = useState(false);
  const { user, token, updateUser } = useAuth();
  const [booking, setBooking] = useState({
    total: 0,
    dateRange: [],
  });

  const { RangePicker } = DatePicker;
  const {
    accommodation,
    getAccommodation,
    isLoading: isLoadingAccommodation,
    error,
  } = useAccommodation();
  const { createBooking, isLoading: isLoadingBooking } = useBooking();
  const { handleShowModal, handleContentModal } = useContext(FormModalContext);
  const { id } = useParams();

  const dialogRef = useRef(null);
  const navigate = useNavigate();


  const handleBackClick = () => {
    navigate(-1);
  };
  useEffect(() => {
    getAccommodation(id);
  }, []);

    if (isLoadingAccommodation) {
    return <Skeleton active />;
  }

  if (error || !accommodation) {
    return <Empty description="No se pudo cargar el alojamiento" />;
  }

  const fullAddress = `${accommodation.address.street}, ${accommodation.address.city}, ${accommodation.address.country}`;
  const handleClickShowImages = () => {
    dialogRef.current.showModal();
  };
  const handleClickCloseDialog = () => {
    dialogRef.current.close();
  };

  const handleAddFavorite = async () => {
    const body = {
      id: user.id,
      favorite: accommodation.id,
    };

    try {
      setIsLoadingFavorite(true);
      const response = await userService.addFavoriteAccommodation(body, token);
      updateUser(response.data);
      toaster["success"]({
        message: "Alojamiento añadido a favoritos.",
        description: "¡Listo!",
        duration: 3,
      });
    } catch (error) {
      toaster["error"]({
        message: "Error al añadir a favoritos.",
        description: error.message,
        duration: 3,
      });
    } finally {
      setIsLoadingFavorite(false);
    }
  };

  const handleRemoveFavorite = async () => {
    const body = {
      id: user.id,
      favorite: accommodation.id,
    };

    try {
      setIsLoadingFavorite(true);
      const response = await userService.removeFavoriteAccommodation(
        body,
        token
      );
      updateUser(response.data);
      toaster["success"]({
        message: "Alojamiento eliminado de favoritos.",
        description: "¡Listo!",
        duration: 3,
      });
    } catch (error) {
      toaster["error"]({
        message: "Error al eliminar de favoritos.",
        description: error.message,
        duration: 3,
      });
    } finally {
      setIsLoadingFavorite(false);
    }
  };

  const handleDateRangeOnChange = (date) => {
    setBooking((prevState) => ({
      ...prevState,
      dateRange: date,
    }));
    if (date !== null && date[0] && date[1]) {
      let days = differenceInDays(date[1], date[0]);
      if (days === 0) days = 1;
      setBooking((prevState) => ({
        ...prevState,
        total: days * accommodation.price,
      }));
    } else {
      setBooking((prevState) => ({
        ...prevState,
        total: 0,
      }));
    }
  };

  const handleBooking = async () => {
    if (
      booking.dateRange === null ||
      !booking.dateRange[0] ||
      !booking.dateRange[1] ||
      booking.total === 0
    ) {
      toaster["error"]({
        message: "Debe completar todos los campos",
        description: "Por favor, seleccione un rango de fechas.",
        duration: 3,
      });
      return;
    }

    const body = {
      id_accommodation: accommodation.id,
      id_user: user.id,
      checkIn: booking.dateRange[0].format("YYYY-MM-DD"),
      checkOut: booking.dateRange[1].format("YYYY-MM-DD"),
      total: booking.total,
    };

    try {
      const data = await createBooking(body);
      navigate(`/confirmar-reserva/${data.id}`);
      toaster["success"]({
        message: "Reserva realizada",
        description: `Se ha realizado la reserva de ${accommodation.name} por un total de $${booking.total}`,
        duration: 3,
      });
    } catch (error) {
      toaster["error"]({
        message: "Error al realizar la reserva",
        description: error.message,
        duration: 3,
      });
    }
  };

  const handleShare = () => {
    handleShowModal();
    handleContentModal(<AccommodationShare accommodation={accommodation} />);
  };

  const images = (images) => {
    const imageList = images || [];

    const displayedImages = [];
    for (let i = 0; i < 5; i++) {
      displayedImages.push(imageList[i] || ImageFallback);
    }
    return (
      <>
        {/* Imagen principal */}
        <figure>
          <img
            src={displayedImages[0]}
            alt="Vista principal del alojamiento"
            onError={(e) => (e.target.src = ImageFallback)}
          />
        </figure>

        {/* Grid de 4 imágenes */}
        <div className="right-grid">
          {displayedImages.slice(1, 5).map((img, index) => (
            <figure key={index}>
              <img
                src={img}
                alt={`Vista ${index + 1} del alojamiento`}
                onError={(e) => (e.target.src = ImageFallback)}
              />
            </figure>
          ))}
        </div>
      </>
    );
  };

  const disabledDate = (current) => {
    const unavailableDates = accommodation?.unavailableDates?.map((date) =>
      dayjs(date)
    );
    if (current && current < dayjs().startOf("day")) {
      return true;
    }

    if (unavailableDates) {
      return unavailableDates.some((date) => date.isSame(current, "day"));
    }
    return false;
  };

  return (
    <main className="acc-detail-container">
      {isLoadingBooking ? (
        <>
          <Skeleton
            active
            title={{ style: { height: "16px" } }}
            paragraph={{ rows: 0 }}
          />
          <Skeleton
            active
            title={{ width: "100%", style: { height: "700px" } }}
            paragraph={{ rows: 0 }}
          />
        </>
      ) : !accommodation ? (
        <>
          <header className="acc-detail-header">
            <button className="acc-detail-back" onClick={handleBackClick}>
              <ArrowLeftOutlined />
              Volver atras
            </button>
          </header>
          <Empty description="No se encontro el alojamientos" />
        </>
      ) : (
        <>
          <header className="acc-detail-header">
            <button className="acc-detail-back" onClick={handleBackClick}>
              <ArrowLeftOutlined />
              Volver atras
            </button>
            <div className="acc-detail-header-info">
              <h1>{accommodation.name}</h1>
              <div className="acc-detail-header-actions">
                <button className="button-icon" onClick={handleShare}>
                  <ShareAltOutlined />
                </button>
                {user &&
                user.favorites?.some(
                  (item) =>
                    String(item.id) === String(id) || // Compara como strings
                    item.id === id // O compara directamente
                ) ? (
                  <button
                    className="accommodation-card-favorite accommodation-card-favorite-active"
                    onClick={handleRemoveFavorite}
                    disabled={isLoadingFavorite}
                  >
                    {isLoadingFavorite ? (
                      <Loading3QuartersOutlined className="spin-animation" />
                    ) : (
                      <HeartFilled />
                    )}
                  </button>
                ) : (
                  <button
                    className="accommodation-card-favorite"
                    onClick={handleAddFavorite}
                    disabled={isLoadingFavorite}
                  >
                    {isLoadingFavorite ? (
                      <Loading3QuartersOutlined className="spin-animation" />
                    ) : (
                      <HeartOutlined />
                    )}
                  </button>
                )}
              </div>
            </div>
          </header>

          <section className="acc-detail-gallery">
            {images(accommodation?.images)}
            <button
              className="acc-gallery-button"
              onClick={() => handleClickShowImages()}
            >
              <CameraOutlined />
              Mostrar todas las fotos
            </button>
          </section>

          <section className="acc-detail-info">
            <div className="acc-detail-info-container">
              <section>
                <h2>Informacion del alojamiento</h2>
                <div className="acc-detail-description">
                  <p>Direccion</p>
                  <p className="acc-detail-location">
                    {accommodation.address.street}, {accommodation.address.city}
                    -{accommodation.address.country}
                  </p>
                </div>
                <div className="acc-detail-description">
                  <p>Descripcion</p>
                  <p>{accommodation.description}</p>
                </div>
              </section>

              <hr className="separator" />
              <section className="acc-detail-amenities">
                <h2>Amenitities</h2>
                <ul>
                  {accommodation?.amenities?.map((amenity, index) => (
                    <li key={index}>
                      <ReactSVG src={amenity.icon} className="amenity-icon" />
                      <span>{amenity.name}</span>
                    </li>
                  ))}
                </ul>
              </section>
              <hr className="separator" />
              <section>
                <h2>Politicas</h2>
                <div className="acc-detail-description-container">
                  {accommodation.policies.map((policy, index) => (
                    <div key={index} className="acc-detail-description">
                      <p>
                        <strong>{policy.policy}</strong>
                      </p>
                      <p>{policy.description}</p>
                    </div>
                  ))}
                </div>
              </section>
            </div>
            <div className="acc-detail-booking">
              <p className="acc-detail-price-night">
                <span>${accommodation.price}</span> por noche
              </p>
              <RangePicker
                id="date"
                className="form-date-range-picker"
                onChange={handleDateRangeOnChange}
                format={"DD/MM/YYYY"}
                placeholder={["Check-in", "Check-out"]}
                locale={locale}
                disabledDate={disabledDate}
                disabled={isLoadingBooking}
              />
              {user ? (
                <button
                  className="button button-primary"
                  onClick={handleBooking}
                  disabled={isLoadingBooking}
                >
                  {isLoadingBooking ? (
                    <Loading3QuartersOutlined className="spin-animation" />
                  ) : (
                    <CalendarOutlined />
                  )}
                  Reservar
                </button>
              ) : (
                <Link className="button button-base" to="/iniciar-sesion">
                  <UserOutlined />
                  Iniciar sesión
                </Link>
              )}
              <div className="acc-detail-price">
                <p>Total</p>
                <p>${booking.total}</p>
              </div>
            </div>
          </section>
          <hr className="separator" />
          {accommodation.totalReviews === 0 ? (
            <p>Aun no se ha comentado nada sobre este alojamiento</p>
          ) : (
            <section className="acc-detail-reviews">
              <h2>
                <StarFilled /> {numeral(accommodation.avgRating).format("0.0")}{" "}
                • {accommodation.totalReviews} Reviews
              </h2>
              <div className="acc-detail-reviews-comments">
                {isLoadingAccommodation ? (
                  <Skeleton
                    active
                    title={{ style: { height: "16px" } }}
                    paragraph={{ rows: 0 }}
                  />
                ) : error ? (
                  <Empty description="No se pudieron obtener las reviews" />
                ) : (
                  accommodation.reviews.map((review, index) => (
                    <ReviewCard key={index} review={review} />
                  ))
                )}
              </div>
            </section>

          )}

          <hr className="separator"/>
          <section>
            <iframe
              width="100%"
              height="300"
              style={{ border: 0, borderRadius: "12px" }}
              loading="lazy"
              allowFullScreen
              src={`https://www.google.com/maps?q=${encodeURIComponent(
                fullAddress
              )}&output=embed`}
            />
          </section>

          <dialog ref={dialogRef} className="dialog-gallery-dialog">
            <div className="dialog-gallery-container">
              <button
                className="dialog-gallery-close"
                onClick={() => handleClickCloseDialog()}
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  height="24px"
                  viewBox="0 -960 960 960"
                  width="24px"
                  fill="#e8eaed"
                >
                  <path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z" />
                </svg>
              </button>
              <Carousel
                showArrows={true}
                infiniteLoop={true}
                className="gallery-container"
              >
                {accommodation?.images?.map((image, index) => (
                  <div key={index}>
                    <img src={image} alt={accommodation.name} />
                  </div>
                ))}
              </Carousel>
            </div>
          </dialog>
          <Modal />
        </>
      )}
    </main>
  );
};
