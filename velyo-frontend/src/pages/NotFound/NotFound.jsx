import React from 'react'
import "./NotFound.css"
import { Empty } from 'antd'
export const NotFound = () => {
  return (
    <main className='not-found-main'>
        <h1>Error 404</h1>
        <Empty description="Parece que esta pagina se fue de vacaciones."/>
    </main>
  )
}
    