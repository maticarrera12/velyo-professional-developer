import { SearchOutlined } from '@ant-design/icons'
import "./CardEmpty.css"

export const CardEmpty = ({title, description}) => {
  return (
    <section className='empty-card'>
        <p><strong>{title}</strong></p>
        <p>{description}</p>
        <Link to='/search' className='button button-base'><SearchOutlined/>Explorar alojamientos</Link>
    </section>
  )
}
