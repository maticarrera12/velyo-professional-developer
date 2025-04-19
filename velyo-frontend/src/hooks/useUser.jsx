import userService from "../services/users.service";
import { useAuth } from "../auth/hook/useAuth";
import { useState } from "react";
import { message } from "antd";

export const useUser = () => {
  const { token, updateUser } = useAuth();
  const [users, setUsers] = useState(null);
  const [user, setUser] = useState(null);
  const [error, setError] = useState({
    error: false,
    message: null,
  });
  const [isLoading, setIsLoading] = useState(false);
  const [totalPages, setTotalPages] = useState(0);

  // const getUsers = async (page = 0, size = 10) => {
  //   try {
  //     setIsLoading(true);
  //     const response = await userService.getUsers(token);
  //     setUsers(response);
  //     setTotalPages(response.totalPages);
  //     setError(false);
  //   } catch {
  //     console.error("Error fetching users");
  //     setError(true);
  //   } finally {
  //     setIsLoading(false);
  //   }
  // };

  const getUsers = async (page = 0, size = 10) => {
    try {
      setIsLoading(true);
      const response = await userService.getUsers(token, page, size); // ✅ pasar los valores
      setUsers(response); // o response?.data?.data si estás usando Axios con `.data`
      setTotalPages(response.totalPages);
      setError(false);
    } catch {
      console.error("Error fetching users");
      setError(true);
    } finally {
      setIsLoading(false);
    }
  };

  const getUserAuthenticated = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await userService.getUserAuthenticated(token);
      setUser(response);
      updateUser(response);
      return response;
    } catch (error) {
      setError(error.message);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const deleteUser = async (id) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await userService.deleteUser(id, token);
      return response;
    } catch (error) {
      setError(error.message);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const addUser = async (values) => {
    try {
      setIsLoading(true);
      await userService.createUser(values, token);
      setError(false);
    } catch {
      console.error("Error adding user");
      setError(true);
    } finally {
      setIsLoading(false);
    }
  };

  const editUser = async (values) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await userService.editUser(values, token);
      return response;
    } catch (error) {
      setError(error.message);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const updateName = async (values) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await userService.updateName(values, token);
      setUser(response.data);
      return response.data;
    } catch (error) {
      setError(error.message);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const addFavoriteAccommodation = async (values) => {
    try {
      setIsLoading(true);
      await userService.addFavoriteAccommodation(values, token);
      return {
        error: false,
        message: null,
      };
    } catch (error) {
      console.error(error.message);
      return {
        error: true,
        message: error.message,
      };
    } finally {
      setIsLoading(false);
    }
  };
  const removeFavoriteAccommodation = async (values) => {
    try {
      setIsLoading(true);
      await userService.removeFavoriteAccommodation(values, token);
      return {
        error: false,
        message: null,
      };
    } catch (error) {
      console.error(error.message);
      return {
        error: true,
        message: error.message,
      };
    } finally {
      setIsLoading(false);
    }
  };

  return{
    user,
    users,
    error,
    isLoading,
    totalPages,
    getUsers,
    getUserAuthenticated,
    deleteUser,
    addUser,
    updateName,
    editUser,
    addFavoriteAccommodation,
    removeFavoriteAccommodation
  }
};
