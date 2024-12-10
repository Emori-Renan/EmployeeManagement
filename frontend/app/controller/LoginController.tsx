import axios from "axios";

interface LoginPayload {
  usernameOrEmail: string;
  password: string;
}

export const login = async (payload: LoginPayload) => {
    console.log("clickei mesmo", payload)
    const response = await axios.post("http://localhost:8080/auth/login", payload);
    if (response.status === 200) {
        console.log("Login successful", response.data);
      } else {
        console.error("Login failed", response.data);
      }
  try {
    return response.data; // Retorna os dados da resposta da API
  } catch (error: any) {
    throw new Error(error.response?.data?.message || "Login failed");
  }
};