import { Link } from "react-router-dom"
import "./CategoryCard.css"
import { ReactSVG } from "react-svg"

export const CategoryCard = ({category}) => {
    const { id, name, image } = category
  return (
    <Link to={`/search?categories=${id}`}>
    <article className="category-card-container">
      <figure>
        <ReactSVG src={image} className="svg-icon" />
      </figure>
      <h3>{name}</h3>
    </article>
  </Link>
  )
}
