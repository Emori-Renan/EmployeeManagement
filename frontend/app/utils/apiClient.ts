import axios from 'axios';
import { getToken } from './auth';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080', // Your API base URL
    withCredentials: true
});

apiClient.interceptors.request.use(
    (config) => {
        const token = getToken(); // Get the token from storage
        
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
