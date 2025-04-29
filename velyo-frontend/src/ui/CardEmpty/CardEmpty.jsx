import { SearchOutlined } from '@ant-design/icons'
import "./CardEmpty.css"
import { Link } from 'react-router-dom'

export const CardEmpty = ({title, description}) => {
  return (
    <section className='empty-card'>
        <p><strong>{title}</strong></p>
        <p>{description}</p>
        <Link to='/search' className='button button-primary'><SearchOutlined/>Explorar alojamientos</Link>
    </section>
  )
}
