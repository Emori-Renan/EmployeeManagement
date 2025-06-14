import axios from 'axios';
import { getToken } from './auth';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080',
    withCredentials: true
});

apiClient.interceptors.request.use(
    (config) => {
        const token = getToken(); 
        if (token && config.headers) { 
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        console.error('Request error:', error);
        return Promise.reject(error);   
    }
);

export default apiClient;
