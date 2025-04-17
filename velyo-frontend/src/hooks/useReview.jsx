import { useState } from "react";
import { useAuth } from "../auth/hook/useAuth";
import reviewService from "../services/reviews.service";

export const useReview = () => {
  const { token } = useAuth();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [reviews, setReviews] = useState(null);
  const [totalPages, setTotalPages] = useState(0);
  const [success, setSuccess] = useState(false);

  const createReview = async (body) => {
    setIsLoading(true);
    setError(null);
    setSuccess(false);
    try {
      const response = await reviewService.createReview(body, token);
      setSuccess(true);
      return response.data;
    } catch (error) {
      setError(error.message);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const getReviewsByAccommodation = async (id) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await reviewService.getReviewsByAccommodation(id);
      setReviews(response.data);
      setSuccess(true);
      return response;
    } catch (error) {
      setError(error.message);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  return {
    isLoading,
    error,
    reviews,
    totalPages,
    success,
    getReviewsByAccommodation,
    createReview,
  };
};
