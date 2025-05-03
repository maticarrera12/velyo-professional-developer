import { useState } from "react";
import { useAuth } from "../auth/hook/useAuth";
import categoryService from "../services/categories.service";

export const useCategory = () =>{
    const { token } = useAuth();
    const [categories, setCategories] = useState(null);
    const [category, setCategory] = useState(null);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [totalPages, setTotalPages] = useState(0);
    const [success, setSuccess] = useState(false);

    const getCategories = async (page = 0, size = 5) => {
        try{
            setIsLoading(true);
            const response = await categoryService.getCategories(page, size);
            setCategories(response);
            setTotalPages(response.totalPages);
            setError(false)
        }catch{
            console.log("Error fetching categories");
            setError(true)
        }finally{
            setIsLoading(false)
        }
    };

    const getAllCategories = async () => {
        setIsLoading(true);
        setError(null);
        try{
            const response = await categoryService.getAllCategories();
            setCategories(response);
            setSuccess(true);
            return response;
        }catch(error){
            setError(error.message)
            throw error;
        }finally{
            setIsLoading(false)
        }
    };


    const deleteCategory = async (id) => {
        setIsLoading(true)
        setError(null)
        try {
            const response = await categoryService.deleteCategory(id, token)
            setSuccess(true)
            return response
        } catch (error) {
            setError(error.message)
            throw error;
        }finally{
            setIsLoading(false)
        }
    };

    const addCategory = async (data) => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await categoryService.createCategory(data, token);
            setSuccess(true);
            return response;
        } catch (error) {
            setError(error.message);
            throw error;
        } finally {
            setIsLoading(false);
        }
    }

    const editCategory = async (data) => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await categoryService.editCategory(data, token);
            setSuccess(true);
            return response;
        } catch (error) {
            setError(error.message);
            throw error;
        } finally {
            setIsLoading(false);
        }
    }

    return{
        categories,
        category,
        error,
        isLoading,
        totalPages,
        getCategories,
        getAllCategories,
        deleteCategory,
        addCategory,
        editCategory,
    }
}