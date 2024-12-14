// app/store/authSlice.ts
import { createSlice } from '@reduxjs/toolkit';
import { getToken } from '../utils/auth';

const token = typeof window !== 'undefined' ? getToken() : null;

const authSlice = createSlice({
  name: 'auth',
  initialState: {
    token: token ?? null,
    isAuthenticated: !!token,
  },
  reducers: {
    loginSuccess(state, action) {
      state.token = action.payload;
      state.isAuthenticated = true;
    },
    logout(state) {
      state.token = null;
      state.isAuthenticated = false;
    },
  },
});

export const { loginSuccess, logout } = authSlice.actions;
export default authSlice.reducer;
