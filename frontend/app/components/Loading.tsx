// components/LoadingModal.tsx
import React from 'react';

interface LoadingModalProps {
    isLoading: boolean;
    message: string;
}

const LoadingModal: React.FC<LoadingModalProps> = ({ isLoading, message }) => {
    if (!isLoading) return null;

    return (
        <div className="modal modal-open">
            <div className="modal-box">
                <h2 className="text-center">{message}</h2>
                <div className="flex justify-center my-4">
                    <span className="loading loading-ring w-[3.5rem] h-[3.5rem]"></span>
                    <span className="loading loading-ring w-[3rem] h-[3rem]"></span>
                    <span className="loading loading-ring w-[2.5rem] h-[2.5rem]"></span>
                    <span className="loading loading-ring w-[2rem] h-[2rem]"></span>
                    <span className="loading loading-ring w-[2.5rem] h-[2.5rem]"></span>
                    <span className="loading loading-ring w-[3rem] h-[3rem]"></span>
                    <span className="loading loading-ring w-[3.5rem] h-[3.5rem]"></span>
                </div>
                    <div className='flex justify-center'>
                        <img className="w-[10rem] h-[10rem] mx-auto"
                            src="https://media1.giphy.com/media/v1.Y2lkPTc5MGI3NjExb3RkcWF1N2EyM2dhZWhlZXpud2o3YWttdTRibjRzMzg4eml5NXFsMCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/xckpvtJhGi3TpQKRrW/giphy.webp" alt="Loading" />
                    </div>
                <div className="modal-action">
                </div>
            </div>
        </div>
    );
};

export default LoadingModal;
