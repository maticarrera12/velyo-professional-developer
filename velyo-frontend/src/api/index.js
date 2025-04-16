import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080/api"
})

api.interceptors.response.use(
    response => response,
    error =>{
        const requestUrl = error.config.url;
        if (error.response.status === 401 && requestUrl.includes("/auth/login")) {
            alert("Su sesion ha expirado. POr favor vuelva a iniciar sesion.");
            localStorage.removeItem("token");
            localStorage.removeItem("user");
            window.location = "/iniciar-sesion"
        }
        return Promise.reject(error)
    }
)

export default api;