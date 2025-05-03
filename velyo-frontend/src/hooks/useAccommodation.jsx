// import accommodationService from "../services/Accommodations.service";
// import { useAuth } from "../auth/hook/useAuth";
// import { useState } from "react";

// export const useAccommodation = () => {
//   const { token } = useAuth();
//   const [error, setError] = useState(null);
//   const [isLoading, setIsLoading] = useState(false);
//   const [totalPages, setTotalPages] = useState(0);
//   const [success, setSuccess] = useState(false);
//   const [accommodation, setAccommodation] = useState(null);
//   const [accommodations, setAccommodations] = useState(null);

//   const getAccommodations = async (page = 0, size = 10) => {
//     try {
//       setIsLoading(true);
//       const response = await accommodationService.getAccommodations(
//         page,
//         size,
//         token
//       );
//       setAccommodations(response);
//       setTotalPages(response.totalPages);
//       setError(false);
//     } catch {
//       console.error("Error fetching alojamientos");
//       setError(true);
//     } finally {
//       setIsLoading(false);
//     }
//   };

//   const getAccommodation = async (id) => {
//     try {
//       setIsLoading(true);
//       const response = await accommodationService.getAccommodation(id);
//       response ? setAccommodation(response) : setAccommodation(null);
//     } catch {
//       console.error("Error obteniendo alojamientos");
//     } finally {
//       setIsLoading(false);
//     }
//   };

//   const getRandomAccommodations = async () => {
//     setIsLoading(true);
//     setError(null);
//     try {
//       const response = await accommodationService.getRandomAccommodations();
//       setAccommodations(response);
//       setSuccess(true);
//       return response;
//     } catch (error) {
//       setError(error.message);
//       throw error;
//     } finally {
//       setIsLoading(false);
//     }
//   };

//   const searchAccommodations = async (
//     categoryIds = [],
//     searchTerm = "",
//     checkIn = null,
//     checkOut = null
//   ) => {
//     try {
//       setIsLoading(true);
//       const response = await accommodationService.searchAccommodations(
//         categoryIds,
//         searchTerm,
//         checkIn,
//         checkOut
//       );
//       setAccommodations(response);
//       setError(false);
//     } catch {
//       console.error("Error buscando alojamientos");
//       setError(true);
//     } finally {
//       setIsLoading(false);
//     }
//   };

//   const deleteAccommodation = async (id) => {
//     setIsLoading(true);
//     setError(null);
//     try {
//       const response = await accommodationService.deleteAccommodation(
//         id,
//         token
//       );
//       setSuccess(true);
//       return response;
//     } catch (error) {
//       setError(error.message);
//       throw error;
//     } finally {
//       setIsLoading(false);
//     }
//   };

//   const addAccommodation = async (data) => {
//     setIsLoading(true);
//     setError(null);
//     try {
//       const response = await accommodationService.createAccommodation(
//         data,
//         token
//       );
//       setSuccess(true);
//       return response;
//     } catch (error) {
//       setError(error.message);
//       throw error;
//     } finally {
//       setIsLoading(false);
//     }
//   };

//   const editAccommodation = async (data) => {
//     setIsLoading(true);
//     setError(null);
//     try {
//       const response = await accommodationService.editAccommodation(
//         data,
//         token
//       );
//       setSuccess(true);
//       return response;
//     } catch (error) {
//       setError(error.message);
//       throw error;
//     } finally {
//       setIsLoading(false);
//     }
//   };

//   return{
//     accommodation,
//     accommodations,
//     isLoading,
//     error,
//     success,
//     totalPages,
//     getAccommodation,
//     getAccommodations,
//     getRandomAccommodations,
//     searchAccommodations,
//     deleteAccommodation,
//     addAccommodation,
//     editAccommodation
//   };
// };
import accommodationService from "../services/Accommodations.service";
import { useAuth } from "../auth/hook/useAuth";
import { useState } from "react";

export const useAccommodation = () => {
  const { token } = useAuth();
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [totalPages, setTotalPages] = useState(0);
  const [success, setSuccess] = useState(false);
  const [accommodation, setAccommodation] = useState(null);
  const [accommodations, setAccommodations] = useState(null);
  const [randomAccommodations, setRandomAccommodations] = useState([]); // 🆕 nuevo estado separado

  const getAccommodations = async (page = 0, size = 10) => {
    try {
      setIsLoading(true);
      const response = await accommodationService.getAccommodations(page, size, token);
      console.log("📦 getAccommodations response:", response);
      setAccommodations(response.data); // Asegurate que esto sea el array correcto
      setTotalPages(response.totalPages);
      setError(false);
    } catch {
      console.error("Error fetching alojamientos");
      setError(true);
    } finally {
      setIsLoading(false);
    }
  };

  const getAccommodation = async (id) => {
    try {
      setIsLoading(true);
      const response = await accommodationService.getAccommodation(id);

      setAccommodation(response || null);
    } catch {
      console.error("Error obteniendo alojamiento");
    } finally {
      setIsLoading(false);
    }
  };

  const getRandomAccommodations = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await accommodationService.getRandomAccommodations();
      setRandomAccommodations(response); // 🆕 setear en estado separado
      setSuccess(true);
    } catch (error) {
      setError(error.message);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const searchAccommodations = async (categoryIds = [], searchTerm = "", checkIn = null, checkOut = null) => {
    try {
      setIsLoading(true);
      const response = await accommodationService.searchAccommodations(
        categoryIds,
        searchTerm,
        checkIn,
        checkOut
      );
      setAccommodations(response);
      setError(false);
    } catch {
      console.error("Error buscando alojamientos");
      setError(true);
    } finally {
      setIsLoading(false);
    }
  };

  const deleteAccommodation = async (id) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await accommodationService.deleteAccommodation(id, token);
      setSuccess(true);
      return response;
    } catch (error) {
      setError(error.message);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const addAccommodation = async (data) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await accommodationService.createAccommodation(data, token);
      setSuccess(true);
      return response;
    } catch (error) {
      setError(error.message);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const editAccommodation = async (data) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await accommodationService.editAccommodation(data, token);
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
    accommodation,
    accommodations,
    randomAccommodations, // 🆕 exportar randoms
    isLoading,
    error,
    success,
    totalPages,
    getAccommodation,
    getAccommodations,
    getRandomAccommodations,
    searchAccommodations,
    deleteAccommodation,
    addAccommodation,
    editAccommodation,
  };
};

