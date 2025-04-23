import api from "../api";

const accommodationService = {
    // getAccommodations: async (page,size,token) => {
    getAccommodations: async (page,size) => {
        try {
            const response = await api.get(`/accommodations?page=${page}&size=${size}`);
            return response.data
        } catch {
            console.log("No se pudieron obtener los alojamientos");
            throw new Error('No se pudieron obtener los alojamientos. Por favor, intente nuevamente.');
        }
    },
    getRandomAccommodations: async () => {
        try{
            const response = await api.get("/accommodations/random");
            return response.data;
        }catch{
            throw new Error("No se pudieron obtener los alojamientos. Por favor, intente nuevamente.");
        }
    },
    searchAccommodations: async(categoryIds, searchTerm, checkIn, checkOut) =>{
        categoryIds = categoryIds.map(id=> `categoryIds=${id}`).join('&');
        searchTerm && (categoryIds += `&searchTerm=${searchTerm}`);
        (checkIn && checkOut) && (categoryIds =+ `&checkIn=${checkIn}&checkOut=${checkOut}`);
        try {
            const response = await api.get(`accommodations/search?${categoryIds}`);
            return response.data
        } catch {
            throw new Error('No se pudieron obtener los alojamientos. Por favor, intente nuevamente.');
        }
    },
    getAccommodation: async (id) => {
        try{
            const response = await api.get(`/accommodations/${id}`);
            return response.data
        }catch(error){
            if(error.response.status === 404) throw new Error("Alojamiento no encontrado");
            throw new Error("No se pudo obtener el alojamiento. Por favor, intente nuevamente.")
        }
    },
    deleteAccommodation: async (id,token) =>{
        try {
            return await api.delete(`/accommodations/${id}`,{
                headers:{
                    'Authorization': `Bearer ${token}`
                }
            })
        } catch (error) {
            console.log(error);
            throw new Error("No se pudo eliminar el alojamiento. Por favor, intente nuevamente.")
                    }
    },
    createAccommodation: async (data, token) =>{
        const formData = new FormData();
        const accommodation = {
            name: data.name,
            price: data.price,
            description: data.description,
            address: data.address,
            category_id: data.category_id,
            amenities: data.amenities,
            policies: data.policies
        }
        formData.append('accommodation', new Blob([JSON.stringify(accommodation)], {type:"application/json"}));
        data.images.forEach(image =>{
            formData.append('images', image);
        });
        
        return api.post("/accommodations", formData, {
            headers:{
                'Accept': 'application/json',
                'Content-Type': 'multipart/form-data',
                'Authorization': `Bearer ${token}`
            }
        }).then(response =>{
            return response.data
        }).catch(error =>{
            console.log(error);
            throw new Error("No se pudo crear el alojamiento. Por favor, intente nuevamente.")
        })
    },
    editAccommodation: async (data,token) => {
        console.log("amenities:", data.amenities)
        const formData = new FormData();
        const accommodation = {
            id: data.id,
            name: data.name,
            price: data.price,
            description: data.description,
            address: data.address,
            category_id: data.category_id,
            amenities: data.amenities,
            policies: data.policies
        }
        formData.append("accommodation", new Blob([JSON.stringify(accommodation)], {type: 'application/json'}));
        data.images?.forEach(image=>{
            formData.append('images', image)
        });
        data.imagesToDelete.length > 0 && formData.append("imagesToDelete", new Blob([JSON.stringify(data.imagesToDelete)], {type:"application/json"}))
        
        return api.put("/accommodations", formData, {
            headers:{
                'Accept': 'application/json',
                'Content-Type': 'multipart/form-data',
                'Authorization': `Bearer ${token}`
            }
        }).then(response =>{
            return response.data
        }).catch(error=>{
            console.log(error);
            throw new Error("No se pudo editar el alojamiento. Por favor, intente nuevamente.")
        })
    }
}

export default accommodationService