import { useEffect } from "react"
import { useAuth } from "../../auth/hook/useAuth"
import { useUser } from "../../hooks/useUser"
import "./AccommodationFavorites.css"
import { HeartFilled } from "@ant-design/icons"
import { AccommodationCard } from "../../components/AccommodationCard/AccommodationCard"
import { CardEmpty } from "../../ui/CardEmpty/CardEmpty"

export const AccommodationFavorites = () => {

    const {getUserAuthenticated, isLoading} = useUser()
    const {user} = useAuth()

    useEffect(()=>{
        getUserAuthenticated()
    }, [])
  return (
    <main className="favorites-container">
        <h2>Alojamientos que me encantan</h2>
        <p className="favorites-count"> <HeartFilled/> {user?.favorites.length} Alojamientos guardados</p>
       

        <div className="favorites-description">
        <p><strong>Accedé a tus alojamientos favoritos desde cualquier lugar</strong></p>
        <p>En esta página se mostrarán los alojamientos favoritos del usuario</p>
        </div>

        <hr className="separator"/>
        <section className="favorites-list">
            {
                user?.favorites && user.favorites.length > 0 ?
                user.favorites?.map((favorite, index)=>{
                    return <AccommodationCard accommodation={favorite} key={index}/>
                }) :
                <CardEmpty title="Todavia no tenes alojamientos en favoritos!"
                    description="Explora alojamientos y guardalos para planificar tu proximo viaj facilmente."/>
            }
        </section>
    </main>
  )
}
