import { useState } from "react";
import { useAuth } from "../auth/hook/useAuth"
import bookingService from "../services/bookings.service";

export const useBooking = () =>{

    const { token } = useAuth();
    const [isLoading, setIsLoading] = useState()
    const [bookings, setBookings] = useState(null)
    const [error, setError] = useState(null)
    const [totalPages, setTotalPages] = useState(0)
    const [success, setSuccess] = useState(false)

    const createBooking = async (body) => {
        setIsLoading(true);
        setError(null);
        setSuccess(false);
    
        try {
            const response = await bookingService.createBooking(body, token);
            setSuccess(true);
            return response.data;
        } catch (error) {
            setError(error.message);
            throw error;
        } finally {
            setIsLoading(false);
        }
    };
    
    const getBookingByUser = async (date) => {
        setIsLoading(true);
        setError(null);
        setSuccess(false);
    
        try {
            const response = await bookingService.getBookingByUser(token, date);
            setBookings(response.data);
            setSuccess(true);
            return response.data;
        } catch (error) {
            setError(error.message);
            throw error;
        } finally {
            setIsLoading(false);
        }
    };
    
    const confirmBooking = async (id) => {
        setIsLoading(true);
        setError(null);
        setSuccess(false);
    
        try {
            const response = await bookingService.confirmBooking(id, token);
            setSuccess(true);
            return response.data;
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
        bookings,
        totalPages,
        success,
        createBooking,
        getBookingByUser,
        confirmBooking
    };
}