// app/store/store.ts
import { configureStore } from '@reduxjs/toolkit';
import authReducer from './authSlice';
import { authMiddleware } from './middleware';

const store = configureStore({
  reducer: {
    auth: authReducer,
  },
  middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(authMiddleware),
});

export default store;
