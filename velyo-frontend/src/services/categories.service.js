import api from "../api";


const categoryService = {
    getCategories: async (page,size) => {
        try {
            const response = await api.get(`/categories?page=${page}&size=${size}`);
            return response.data;
        } catch {
            throw new Error('No se pudieron obtener las categorías. Por favor, intente nuevamente.');
        }
    },
    getAllCategories: async () => {
        try {
            const response = await api.get('/categories/all');
            return response.data;
        } catch {
            throw new Error('No se pudieron obtener las categorías. Por favor, intente nuevamente.');
        }
    },
    getCategory: async (id) => {
        try {
            const response = await api.get(`/categories/${id}`);
            return response.data;
        } catch (error) {
            if (error.response.status === 404) throw new Error('Categoría no encontrada.');
            throw new Error('No se pudo obtener la categoría. Por favor, intente nuevamente.');
        }
    },
    deleteCategory: async (id, token) => {
        try {
            return await api.delete(`/categories/${id}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
        } catch (error) {
            console.error(error);
            throw new Error(error.response.data);
        }
    },
    createCategory: async (data, token) => {
        const formData = new FormData();
        const category = {
            name: data.name,
            description: data.description
        };
        formData.append('category', new Blob([JSON.stringify(category)], { type: 'application/json' }));
        data.image[0] && formData.append('image', data.image[0]);

        try {
            return await api.post('/categories', formData, {
                headers: {
                    'Accept': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
        } catch (error) {
            console.error(error);
            throw new Error('No se pudo crear la categoría. Por favor, intente nuevamente.');
        }
    },
    editCategory: async (data, token) => {
        const formData = new FormData();
        const category = {
            id: data.id,
            name: data.name,
            description: data.description
        };
        formData.append('category', new Blob([JSON.stringify(category)], { type: 'application/json' }));
        data.image && formData.append('image', data.image[0]);

        try {
            return await api.put('/categories', formData, {
                headers: {
                    'Accept': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
        } catch (error) {
            console.error(error);
            throw new Error('No se pudo editar la categoría. Por favor, intente nuevamente');
        }
    }
};

export default categoryService;