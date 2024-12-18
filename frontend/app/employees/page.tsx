"use client"
import { FormEvent, useState } from "react";
import ErrorMessage from "../components/ErrorMessage";
import withAuth from "../store/withAuth"
import { useRouter } from "next/navigation";
import LoadingModal from "../components/Loading";
import { delay } from "../utils/functions";
import { registerEmployee } from "../controller/EmployeeController";

const Employees = () => {

    const [username, setUsername] = useState("");
    const [usernameError, setUsernameError] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(false);
    const router = useRouter();

    const validateForm = () => {
        setUsernameError(false);
        setError(null);
        if (username.length == 0) {
            setError("Username is mandatory");
            setUsernameError(true);
            return false;
        }
    }

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();

        if (!validateForm()) {
            return;
        }

        try {
            setIsLoading(true);
            const response = await registerEmployee({
                            name: username,
                            role: "employee"
                        });
                        
                        if (!response.success) {
                            throw new Error(response.message);
                        }
                        await delay(4000);
                        router.push("/");
                        return;
        } catch (err:any) {
            
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <div className="hero bg-base-200 min-h-screen">
            <div className="hero-content flex-col ">
                <div className="text-center lg:text-left">
                    <h1 className="text-5xl font-bold">Complete your profile</h1>
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
                            <input type="text" className="grow" placeholder="Name" name="username"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)} />
                        </label>
                        <ErrorMessage errorType={usernameError} message={error} />
                        <div className="form-control mt-6">
                            <button className="btn btn-primary">Register</button>
                        </div>
                    </form>
                </div>
            </div>
            <LoadingModal isLoading={isLoading} message="Loading..." />
        </div>);
}

export default withAuth(Employees);