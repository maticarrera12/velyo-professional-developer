import { useContext, useState } from "react"
import "./AccommodationShare.css"
import { NotificationContext } from "../../context/NotificationContext"

export const AccommodationShare = ({accommodation}) => {

    const [message, setMessage] = useState("Mira este increible Alojamiento!")
    const {toaster} = useContext(NotificationContext)

    const shareAccommodation = (platform) =>{
        const url = window.location.href;
        let shareLink;
        if(platform === 'copy'){
            navigator.clipboard.writeText(url)
            toaster['success']({
                message: 'Enlace copiado',
                description: 'El enlace de la estancia ha sido copiado al portapapeles.',
                duration: 2
            })
            return
        }
        if (platform === 'facebook') {
            shareLink = `https://www.facebook.com/sharer/sharer.php?u=${url}`;
        }
        if (platform === 'whatsapp') {
            const encodedMessage = encodeURIComponent(`${message} ${url}`);
            shareLink = `https://web.whatsapp.com/send?text=${encodedMessage}`;
        }
        if (platform === 'twitter') {
            shareLink = `https://twitter.com/intent/tweet?url=${url}&text=${message}`;
        }
        window.open(shareLink, '_blank');
    }

    const handleOnChange = (e)=>{
        setMessage(e.target.value)
    }
  return (
    <section className="accommodation-share-container">
        <h2>Compartir alojamiento!</h2>
        <figure className="accommodation-share-figure">
            <img src={accommodation?.images[0]} alt={accommodation?.name} width={50} height={50} />
            <figcaption>{accommodation?.name}</figcaption>
        </figure>
        <form className="form-container">
            <label htmlFor="text" className="form-label" aria-labelledby="text">
                <textarea
                name="text"
                id="text"
                rows="4"
                placeholder="Escribe aca tu mensaje para compartir el alojamiento"
                onChange={handleOnChange}
                value={message}
                />
            </label>
        </form>

        <div className="accommodation-share-social-networks">
                <button className="button button--outline" onClick={() => shareAccommodation('copy')}>
                    <CopyOutlined />
                    Copiar enlace
                </button>
                <button className="button button--outline" onClick={() => shareAccommodation('whatsapp')}>
                    <WhatsAppOutlined />
                    Whatsapp
                </button>
                <button className="button button--outline" onClick={() => shareAccommodation('twitter')}>
                    <XOutlined />
                    Twitter
                </button>
                <button className="button button--outline" onClick={() => shareAccommodation('facebook')}>
                    <FacebookFilled />
                    Facebook
                </button>
            </div>
    </section>
  )
}
