import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080', // Your API base URL
});

apiClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token'); // Get the token from storage
        if (token && config.headers) { // Ensure headers exist
            config.headers['Authorization'] = `Bearer ${token}`; // Add token to headers
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default apiClient;
