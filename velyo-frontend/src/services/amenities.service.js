import api from "../api";

const amenityService = {

    getAmenities: async (page,size) => {
        try {
            const response = await api.get(`/amenities?page=${page}&size=${size}`);
            return response.data;
        } catch {
            throw new Error("No se pudieron obtener las caracteristicas. Por favor, intente nuevamente.");
        }
    },

    getAllAmenities: async () => {
        try {
            const response = await api.get('/amenities/all');
            return response.data;
        } catch {
            throw new Error("No se puedieron obtener las caracteristicas. Por favor, intente nuevamente.")
        }
    },
    getAmenity: async (id) =>{
        try {
            const response = await api.get(`/amenities/${id}`);
            return response.data
        } catch (error) {
            if(error.response.status === 404) throw new Error("Caracteristica no encontrada");
            throw new Error("No se pudo obtener la caracteristica. Por favor, intente nuevamente.")
        }
    },
    deleteAmenity:  async (id) => {
        try {
            const response = await api.get(`/amenities/${id}`);
            return response.data;
        } catch (error) {
            if (error.response.status === 404) throw new Error('Característica no encontrada');
            throw new Error('No se pudo obtener la característica. Por favor, intente nuevamente.');
        }
    },
    createAmenity: async (data, token) => {
        const formData = new FormData();
        const amenity = {
            name: data.name,
        };
        formData.append('amenity', new Blob([JSON.stringify(amenity)], { type: 'application/json' }));
        data.icon[0] && formData.append('icon', data.icon[0]);

        try {
            return await api.post('/amenities', formData, {
                headers: {
                    'Accept': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
        } catch (error) {
            console.error(error);
            throw new Error('No se pudo crear la característica. Por favor, intente nuevamente.');
        }
    },
    editAmenity: async (data, token) => {
        const formData = new FormData();
        const amenity = {
            id: data.id,
            name: data.name,
        };
        formData.append('amenity', new Blob([JSON.stringify(amenity)], { type: 'application/json' }));
        data.icon && formData.append('icon', data.icon[0]);

        try {
            return await api.put('/amenities', formData, {
                headers: {
                    'Accept': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
        } catch (error) {
            console.error(error);
            throw new Error('No se pudo editar la característica. Por favor, intente nuevamente.');
        }
    }
}

export default amenityService