"use client"
import { FormEvent, useState } from "react";
import { login } from "../controller/LoginController";
import { AuthError } from "../errors/AuthError";
import { useRouter } from 'next/navigation'
import LoadingModal from "../components/Loading";
import { useDispatch } from "react-redux";
import { loginSuccess } from "../store/authSlice";
import { saveToken } from "../utils/auth";
import { delay } from "../utils/functions";
import { useToast } from "../context/ToastContext";



export default function LoginPage() {


    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [usernameError, setUsernameError] = useState(false);
    const [passwordError, setPasswordError] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const [isLoading, setIsLoading] = useState(false);
    const { showToast } = useToast();

    const dispatch = useDispatch();
    const router = useRouter();

    const validateForm = () => {
        setUsernameError(false);
        setPasswordError(false);
        setError(null);

        if (username == "") {
            setUsernameError(true);
            setError("Username is required!");
            return false;
        }
        if (password == "") {
            setPasswordError(true);
            setError("A password is required!");
            return false;
        }
        return true;
    }

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        if (!validateForm()) {
            return;
        }
        try {
            const { searchParams } = new URL(window.location.href);
            const redirectPath = searchParams.get("redirect") || "/";
            setIsLoading(true);
            const data = await login({ usernameOrEmail: username, password: password });
            if (data instanceof AuthError) {
                throw data;
            }
            const token = data.token ?? ''
            dispatch(loginSuccess(token));
            saveToken(token);
            await delay(3000);
            router.push(redirectPath);
            showToast("User logged in successfully!", "success");
            return;
        } catch (err: any) {
            console.log("deu erro ai patrao ", err.statusCode);

            setUsernameError(false);
            setPasswordError(false);
            setError(null);

            if (err.statusCode === 404) {
                setUsernameError(true);
                setError("User not found");
            } else if (err.statusCode === 401) {
                setPasswordError(true);
                setError("Invalid credentials");
            } else {
                throw new AuthError("An unexpected error occurred", 500);
            }
        } finally {
            setIsLoading(false);
        }
    };


    return (
        <div className="hero bg-base-200 min-h-screen">
            <div className="hero-content flex-col ">
                <div className="text-center lg:text-left">
                    <h1 className="text-5xl font-bold">Login now!</h1>
                    <p className="py-6">
                        Provident cupiditate voluptatem et in.
                    </p>
                </div>
                <div className="card bg-base-100 w-full max-w-sm shrink-0 shadow-2xl">
                    <form className="card-body gap-0" onSubmit={handleSubmit}>
                        <label className={`input input-bordered flex items-center
                             ${usernameError ? ' input-error w-full max-w-xs' : 'mb-3'}`}>
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                viewBox="0 0 16 16"
                                fill="currentColor"
                                className="h-4 w-4 opacity-70">
                                <path
                                    d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6ZM12.735 14c.618 0 1.093-.561.872-1.139a6.002 6.002 0 0 0-11.215 0c-.22.578.254 1.139.872 1.139h9.47Z" />
                            </svg>
                            <input type="text" className="grow" placeholder="Username" name="username"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)} />
                        </label>
                        {usernameError &&
                            <div className="label p-0 p-2 pt-0">
                                <span className="label-text-alt text-error">{error}</span>
                            </div>
                        }
                        <label className={`input input-bordered flex items-center
                             ${passwordError ? ' input-error w-full max-w-xs' : ''}`}>
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                viewBox="0 0 16 16"
                                fill="currentColor"
                                className="h-4 w-4 opacity-70">
                                <path
                                    fillRule="evenodd"
                                    d="M14 6a4 4 0 0 1-4.899 3.899l-1.955 1.955a.5.5 0 0 1-.353.146H5v1.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-2.293a.5.5 0 0 1 .146-.353l3.955-3.955A4 4 0 1 1 14 6Zm-4-2a.75.75 0 0 0 0 1.5.5.5 0 0 1 .5.5.75.75 0 0 0 1.5 0 2 2 0 0 0-2-2Z"
                                    clipRule="evenodd" />
                            </svg>
                            <input type="password" className="grow" placeholder="Password" name="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)} />
                        </label>
                        {passwordError &&
                            <div className="label pt-0">
                                <span className="label-text-alt text-error">{error}</span>
                            </div>
                        }
                        <div className="form-control mt-6">
                            <button className="btn btn-primary">Login</button>
                        </div>
                    </form>
                </div>
            </div>
            <LoadingModal isLoading={isLoading} message="Signing up" />
        </div>
    )
}