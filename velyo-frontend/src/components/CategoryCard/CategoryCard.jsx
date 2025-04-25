import { Link } from "react-router-dom"
import "./CategoryCard.css"

export const CategoryCard = ({category}) => {
    const { id, name, image } = category
  return (
    <Link to={`/search?categories=${id}`}>
        <article className="category-card-container">
            <figure>
                <img src={image} alt={name} />
            </figure>
            <h3>{name}</h3>
        </article>
    </Link>
  )
}
