import { StarFilled } from "@ant-design/icons"
import "./ReviewForm.css"
import { format } from 'date-fns'
const stars = (ratingScore) => {
    const stars = []
    for(let i = 0; i < ratingScore; i++){
        stars.push(<StarFilled key={i}/>)
    }
    return stars
}
export const ReviewForm = ({review}) => {


  return (
    <article className="review-card-container">
        <header className="review-card-header">
            <div className="review-card-avatar">{review.user.firstname[0]} {review.user.lastname[0]}</div>
            <p className="review-card-stars">
                {stars(review.rating)}
            </p>
            <div className="review-card-info">
                <p>{review.user.firstname} {review.user.lastname}</p>
                <p>{format(review.reviewDate, 'dd/MM/yyyy - HH:mm')}</p>
            </div>
        </header>
    </article>
  )
}
