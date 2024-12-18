// app/store/authSlice.ts
import { createSlice } from '@reduxjs/toolkit';

// Define the initial state without token
const initialState = {
  token: null,
  isAuthenticated: false,
  username: null
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    loginSuccess(state, action) {
      state.token = action.payload;
      state.isAuthenticated = true;
    },
    logout(state) {
      state.token = null;
      state.isAuthenticated = false;
    },
    setToken(state, action) {
      state.token = action.payload;
      state.isAuthenticated = !!action.payload;
    }, 
    setUsername(state, action) {
      state.username = action.payload;
    }
  },
});

export const { loginSuccess, logout, setToken, setUsername } = authSlice.actions;
export default authSlice.reducer;
