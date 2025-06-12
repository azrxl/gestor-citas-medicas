import axios from 'axios';

// Esta función auxiliar simple lee el token desde el localStorage.
const getAuthToken = (): string | null => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
        try {
            const user = JSON.parse(storedUser);
            return user?.token || null;
        } catch (error) {
            return null;
        }
    }
    return null;
};

// 1. Creamos una instancia de Axios con la URL base.
const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api'
});

// 2. Usamos un "interceptor" para modificar cada petición ANTES de que se envíe.
apiClient.interceptors.request.use(
    (config) => {
        // Obtenemos el token.
        const token = getAuthToken();
        // Si el token existe, lo añadimos a la cabecera 'Authorization'.
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        // Manejamos errores en la configuración de la petición.
        return Promise.reject(error);
    }
);

export default apiClient;