import api from "../../api";

const authService = {
    login: async (body) => {
        try {
            const response = await api.post('/auth/login', body);
            return response.data;
        } catch (error) {
            const status = error.response?.status;

            switch (status) {
                case 401:
                    throw new Error('El email o la contraseña son incorrectos.');
                case 404:
                    throw new Error('El usuario no se encuentra registrado en el sistema.');
                default:
                    if (error.message === 'Network Error') {
                        throw new Error('Verifique su conexión a Internet.');
                    }
                    throw new Error('No se pudo iniciar sesión. Por favor, intente nuevamente.');
            }
        }
    },

    register: async (body) => {
        try {
            const response = await api.post('/auth/register', body);
            return response.data;
        } catch (error) {
            const status = error.response?.status;

            switch (status) {
                case 409:
                    throw new Error('El usuario con ese email ya se encuentra registrado en el sistema.');
                default:
                    if (error.message === 'Network Error') {
                        throw new Error('Verifique su conexión a Internet.');
                    }
                    throw new Error('El usuario no pudo ser registrado. Por favor, intente nuevamente.');
            }
        }
    }
};


export default authService;