import { useContext, useState } from "react";
import { useAuth } from "../../auth/hook/useAuth";
import { NotificationContext } from "../../context/NotificationContext";
import userService from "../../services/users.service";
import "./AccommodationCard.css";
import { HeartFilled, HeartOutlined, Loading3QuartersOutlined } from "@ant-design/icons";
import { Link } from "react-router-dom";
import numeral from "numeral";

export const AccommodationCard = ({ accommodation }) => {
  const [isLoading, setIsLoading] = useState(false);
  const { toaster } = useContext(NotificationContext);
  const { user, token, updateUser } = useAuth();

  const handleAddFavorite = async () => {
    const body = {
      id: user.id,
      favorite: accommodation.id,
    };

    try {
      setIsLoading(true);
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
      setIsLoading(false);
    }
  };

  const handleRemoveFavorite = async () => {
    const body = {
      id: user.id,
      favorite: accommodation.id,
    };

    try {
      setIsLoading(true);
      const response = await userService.removeFavoriteAccommodation(
        body,
        token
      );
      updateUser(response.data);
      toaster["success"]({
        message: "Alojamiento eliminado de favoritos.",
        description: "Listo!",
        duration: 3,
      });
    } catch (error) {
      toaster["error"]({
        message: "Error al eliminar de favoritos.",
        description: error.message,
        duration: 3,
      });
    } finally {
      setIsLoading(false);
    }
  };

  const { id, images, name, price, address, avgRating, totalReviews } =
    accommodation;
  return (
    <article className="accommodation-card">
        {
            user && (
                user.favorites.find(item => {
                    return item.id === id
                }) ? (
                    <button className="accommodation-card-favorite accommodation-card-favorite-active" onClick={handleRemoveFavorite} disabled={isLoading}>
                        {isLoading ? <Loading3QuartersOutlined className='spin-animation'/> : <HeartFilled/>}
                    </button>
                ): (
                    <button className="accommodation-card-favorite" onClick={handleAddFavorite} disabled={isLoading}>
                        {isLoading? <Loading3QuartersOutlined className="spin-animation"/> : <HeartOutlined/>}
                    </button>
                )
            )
        }
        <Link to={`/alojamientos/${id}`}>
        <article className="accommodation-card-container">
            <figure>
                <img src={images[0]} alt="Alojamiento" height={250} width={250}/>
            </figure>
            <div className="accommodation-card-info">
                <h3 className="accommodation-card-tile">{name}</h3>
                <p>{address.country} - {address.city}</p>
                {
                    totalReviews === 0 || !totalReviews ? <p className="stay-card-score">Sin Calificaciones</p> : <p className="accommodation-card-score"><span>{numeral(avgRating).format('0.0')}</span>{totalReviews} Calificaciones</p>
                }
                <p className="accommodation-card-price"><small>Por noche</small><strong>${price}</strong></p>
            </div>
        </article>
        </Link>
    </article>
  );
};
