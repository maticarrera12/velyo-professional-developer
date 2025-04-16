import api from "../api";


const reviewService = {
    createReview: async (body, token) => {
        return api.post('/reviews', body, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        }).catch(() => {
            throw new Error('No se pudo crear la review. Por favor, intente nuevamente.');
        });
    },

    getReviewsByAccommodation: async (id) => {
        return api.get(`/reviews/accommodation/${id}`)
            .then(response => {
                return response.data;
            }).catch(() => {
                throw new Error('No se pudieron obtener las reviews. Por favor, intente nuevamente.');
            });
    }
}

export default reviewService;