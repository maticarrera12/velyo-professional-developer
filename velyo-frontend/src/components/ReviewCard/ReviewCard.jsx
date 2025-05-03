import { StarFilled } from "@ant-design/icons"
import "./ReviewCard.css"
import { format } from 'date-fns'
import { Avatar } from "antd"

const stars = (ratingScore) => {
    const stars = []
    for(let i = 0; i < ratingScore; i++){
        stars.push(<StarFilled key={i} className="review-star"/>)
    }
    return stars
}

export const ReviewCard = ({review}) => {
  return (
    <article className="review-card-container">
        <header className="review-card-header">
            <div className="review-card-top-line">
                <Avatar className="review-card-avatar" size={40}>
                    {review.user.firstname[0].toUpperCase()}{review.user.lastname[0].toUpperCase()}
                </Avatar>
                <p className="review-card-stars">
                    {stars(review.rating)}
                </p>
            </div>
            <div className="review-card-info">
                <p className="review-card-user-name">{review.user.firstname} {review.user.lastname}</p>
                <p className="review-card-date">{format(review.reviewDate, 'dd/MM/yyyy - HH:mm')}</p>
                <p className="review-card-comment">{review.comment ?? "No ha agregado un comentario"}</p>
            </div>
        </header>
    </article>
  )
}