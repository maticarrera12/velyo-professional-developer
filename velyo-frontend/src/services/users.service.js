import api from "../api";


const userService = {
    getUsers: async (token) => {
        try {
            const response = await api.get('/users', {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            return response.data;
        } catch {
            throw new Error('No se pudieron obtener los usuarios. Por favor, intente nuevamente.');
        }
    },
    deleteUser: async (id, token) => {
        try {
            return await api.delete(`/users/${id}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
        } catch (error) {
            console.error(error);
            throw new Error('No se pudo eliminar el usuario. Por favor, intente nuevamente.');
        }
    },
    getUserAuthenticated: async (token) => {
        try {
            const response = await api.get('/users/me', {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            return response.data;
        } catch {
            throw new Error('No se pudo obtener el usuario. Por favor, intente nuevamente.');
        }
    },
    createUser: async (values, token) => {
        try {
            return await api.post('/users', values, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
        } catch (error) {
            throw new Error(error.response.data);
        }
    },
    editUser: async (values, token) => {
        try {
            return await api.put('/users', values, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
        } catch (error) {
            console.log(error);
            throw new Error(error.response.data);
        }
    },
    updateName: async (values, token) => {
        try {
            return await api.put('/users/update-name', values, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
        } catch (error) {
            console.error(error);
            throw new Error('No se pudo actualizar el nombre. Por favor, intente nuevamente.');
        }
    },
    addFavoriteStay: async (values, token) => {
        try {
            return await api.post('/users/add-favorite', values, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
        } catch {
            throw new Error('No se pudo agregar a favoritos. Por favor, intente nuevamente.');
        }
    },
    removeFavoriteStay: async (values, token) => {
        try {
            return await api.post('/users/remove-favorite', values, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
        } catch {
            throw new Error('No se pudo eliminar de favoritos. Por favor, intente nuevamente.');
        }
    }
}

export default userService;