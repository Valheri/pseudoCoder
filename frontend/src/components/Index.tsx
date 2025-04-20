import React from 'react';
import { useNavigate } from 'react-router-dom';

interface IndexProps {
    onEnter: () => void;
}

const Index: React.FC<IndexProps> = () => {
    const navigate = useNavigate();

    const handleEnter = () => {
        navigate('/pseudoCodes'); // Navigate to the PseudoCodes list
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-50 p-4">
            <div className="max-w-lg w-full bg-white p-10 rounded-lg shadow-lg text-center">
                <h1 className="text-4xl font-bold text-gray-800 mb-8">Hello, welcome to PseudoCoder!</h1>
                <button 
                    onClick={handleEnter} 
                    className="bg-blue-500 hover:bg-blue-600 text-white font-semibold text-lg px-8 py-3 rounded-md transition-colors duration-300 shadow-md hover:shadow-lg"
                >
                    Enter
                </button>
            </div>
        </div>
    );
};

export default Index;
