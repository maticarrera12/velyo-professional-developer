import React from 'react'
import "./AuthLayout.css"
import { Link } from 'react-router-dom'
export const AuthLayout = ({children}) => {
  return (
    <main className='auth-layout'>
        <section className='auth-info-section'>
            <h1><Link to="/">Velyo</Link></h1>
            <p>Todos los alojamientos a un click de distancia.</p>
        </section>
        <section className='auth-form-section'>
            {children}
        </section>
    </main>
  )
}
