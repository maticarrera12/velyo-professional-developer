import { useState } from "react";
import { useAuth } from "../auth/hook/useAuth";
import amenityService from "../services/amenities.service";

export const useAmenity = () => {
  const { token } = useAuth();
  const [amenities, setAmenities] = useState(null);
  const [amenity, setAmenity] = useState(null);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [totalPages, setTotalPages] = useState(0);
  const [success, setSuccess] = useState(false);

  const getAmenities = async (page = 0, size = 5) => {
    try {
      setIsLoading(true);
      const response = await amenityService.getAmenities(page, size);
      setAmenities(response);
      setTotalPages(response.totalPages);
      setError(false);
    } catch{
      console.error("Error fetching features");
      setError(true);
    } finally {
        setIsLoading(false);
    }
  };

  const getAllAmenities = async () => {
    setIsLoading(true);
    setError(null);
    setSuccess(false);
    try {
      const response = await amenityService.getAllAmenities();
      setAmenities(response);
      setSuccess(true);
      return response;
    } catch (error) {
      setError(error.message);
      throw error;
    } finally {
        setIsLoading(false);
    }
  };
  const deleteAmenity = async (id) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await amenityService.deleteAmenity(id, token);
      return response;
    } catch (error) {
      setError(error.message);
      throw error;
    } finally {
        setIsLoading(false);
    }
  };

  const addAmenity = async (data) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await amenityService.createAmenity(data, token);
      setSuccess(true);
      return response;
    } catch (e) {
      setError(e.message);
      throw e;
    } finally {
        setIsLoading(false);
    }
  };

  const editAmenity = async (data) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await amenityService.editAmenity(data, token);
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
    amenities,
    amenity,
    error,
    isLoading,
    totalPages,
    getAmenities,
    getAllAmenities,
    deleteAmenity,
    addAmenity,
    editAmenity,
  };
};
