import React, { useContext } from 'react'
import Isologo  from "../../assets/images/velyo_logotipo.svg"
import { Link } from 'react-router-dom'
import { NotificationContext } from '../../context/NotificationContext'
import { FaWhatsapp } from "react-icons/fa";
import { FaLinkedin } from "react-icons/fa6";
import "./Footer.css"
export const Footer = () => {

  const { toaster } = useContext(NotificationContext);
  const handleWhatsapp = (e)=>{
    e.preventDefault()
    try {
      const newWindow = window.open('https://wa.me/5491154793056?text=¡Hola!%20Quisiera%20más%20información%20acerca%20de%20Velyo.', '_blank')
      if(!newWindow || newWindow.closed || typeof newWindow.closed === 'undefined'){
        throw new Error("Por favor permirta los popups para este sitio")
      }
    } catch (error) {
      console.error(error);
      toaster['error']({
        message:"Error al abrir WhatsApp",
        description: "No se pudo abrir WhatsApp.",
        duration: 3
      })
      
    }
  }
  return (
    <footer className='footer'>

        <Link to="/">
     <figure className='footer-isologo'>
        <img className='logo-white' src={Isologo} height={50} width={50} alt="Velyo" />
        <figcaption>&copy; 2025 Velyo</figcaption>
      </figure>
     </Link>
     <div className='footer-socials'>
      <a href="https://wa.me/5491154793056?text=¡Hola!%20Quisiera%20más%20información%20acerca%20de%20Velyo." onClick={handleWhatsapp}>
      <FaWhatsapp size={32}/>
      </a>
      <a href="https://www.linkedin.com/in/matias-carrera-761b45328/">
      <FaLinkedin size={32}/>
      </a>
     </div>
    </footer>
  )
}
