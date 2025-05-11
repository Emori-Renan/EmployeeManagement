"use client"
import { FormEvent, useState } from "react";
import { AuthError } from "../errors/AuthError";
import ErrorMessage from "../components/ErrorMessage";
import { register } from "../controller/RegisterController";
import { useRouter } from 'next/navigation'
import LoadingModal from "../components/Loading";
import { delay } from "../utils/functions";
import { useDispatch } from "react-redux";
import { saveToken } from "../utils/auth";
import { loginSuccess } from "../store/authSlice";
import { login } from "../controller/LoginController";

export default function RegisterPage() {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [usernameError, setUsernameError] = useState(false);
    const [passwordError, setPasswordError] = useState(false);
    const [emailError, setEmailError] = useState(false);
    const [confirmPasswordError, setConfirmPasswordError] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const [isLoading, setIsLoading] = useState(false)

    const dispatch = useDispatch();
    const router = useRouter();

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();

        if(!validateForm()){
            return;
        }
            
        try {
            setIsLoading(true);
            const response = await register({  email: email,
                username: username,
                password: password,
                role: "EMPLOYEE"
            });
            
            if (!response.success) {
                throw new Error(response.message);
            }

            const data = await login({ usernameOrEmail: username, password: password });
                if (data instanceof AuthError) {
                    throw data;
                }

            const token = data.token ?? ''
            dispatch(loginSuccess(token));
            saveToken(token);
            await delay(3000);

            // await delay(4000);
            router.push("/");
            return;

        } catch (err: any) {
            
            if(err == "Error: Email already exists."){
                setError(err.message);
                setEmailError(true);
            }

            if(err == "Error: Username already exists."){
                setError(err.message);
                setUsernameError(true);
            }

            if (err.statusCode === 404) {
                setUsernameError(true);
                setError("User not found");
            } else {
                throw new AuthError("An unexpected error occurred sifudeu" + err, 500);
            }
        } finally {
                setIsLoading(false);
        }
    };

    const validateForm = () => {
        setUsernameError(false);
        setEmailError(false);
        setPasswordError(false);
        setConfirmPasswordError(false);
        setError(null);

        if (!email.includes("@")) {
            setError("Invalid email format");
            setEmailError(true);
            return false;
        }
        if(username.length == 0){
            setError("Username is mandatory");
            setUsernameError(true);
            return false;
        }
        if (username.length < 3 || username.length >20) {
            setError("Username must be between 3 and 20 characters");
            setUsernameError(true);
            return false;
        }
        if (password.length < 6) {
            setError("Password must be at least 6 characters");
            setPasswordError(true);
            return false;
        }
        if (password != confirmPassword) {
            setError("Passwords does not match.");
            setConfirmPasswordError(true);
            return false;
        }
        return true;
    };


    return (
        <div className="hero bg-base-200 min-h-screen">
            <div className="hero-content flex-col ">
                <div className="text-center lg:text-left">
                    <h1 className="text-5xl font-bold">Sign up now!</h1>
                    <p className="py-6">
                        Provident cupiditate voluptatem et in.
                    </p>
                </div>
                <div className="card bg-base-100 w-full max-w-sm shrink-0 shadow-2xl">
                    <form className="card-body gap-0" onSubmit={handleSubmit}>
                        <label className={`input input-bordered flex items-center
                             ${emailError ? ' input-error w-full max-w-xs' : 'mb-3'}`}>
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                viewBox="0 0 16 16"
                                fill="currentColor"
                                className="h-4 w-4 opacity-70">
                                <path
                                    d="M2.5 3A1.5 1.5 0 0 0 1 4.5v.793c.026.009.051.02.076.032L7.674 8.51c.206.1.446.1.652 0l6.598-3.185A.755.755 0 0 1 15 5.293V4.5A1.5 1.5 0 0 0 13.5 3h-11Z" />
                                <path
                                    d="M15 6.954 8.978 9.86a2.25 2.25 0 0 1-1.956 0L1 6.954V11.5A1.5 1.5 0 0 0 2.5 13h11a1.5 1.5 0 0 0 1.5-1.5V6.954Z" />
                            </svg>
                            <input type="text" className="grow" placeholder="Email" name="username"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}/>
                        </label>
                        <ErrorMessage errorType={emailError} message={error} />
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
                        <ErrorMessage errorType={usernameError} message={error} />
                        <label className={`input input-bordered flex items-center
                             ${passwordError ? ' input-error w-full amax-w-xs' : 'mb-3'}`}>
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
                        <ErrorMessage errorType={passwordError} message={error} />
                        <label className={`input input-bordered flex items-center
                             ${confirmPasswordError ? ' input-error w-full max-w-xs' : ''}`}>
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
                            <input type="password" className="grow" placeholder="Confirm Password" name="confirmPassword"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)} />
                        </label>
                        <ErrorMessage errorType={confirmPasswordError} message={error} />
                        <div className="form-control mt-6">
                            <button className="btn btn-primary">Register</button>
                        </div>
                    </form>
                </div>
            </div>
            <LoadingModal isLoading={isLoading} message="Registering..." />
        </div>

    )
}