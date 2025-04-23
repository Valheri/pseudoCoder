import React from 'react';
import { useNavigate } from 'react-router-dom';

interface IndexProps {
    onEnter: () => void;
}

const Index: React.FC<IndexProps> = ({ onEnter }) => {
    const navigate = useNavigate();

    const handleLogin = () => {
        navigate('/login'); // Navigate to the Login page
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-50 p-4">
            <div className="max-w-lg w-full bg-white p-10 rounded-lg shadow-lg text-center">
                <h1 className="text-4xl font-bold text-gray-800 mb-8">Hello, welcome to PseudoCoder!</h1>
                <button 
                    onClick={onEnter} 
                    className="bg-blue-500 hover:bg-blue-600 text-white font-semibold text-lg px-8 py-3 rounded-md transition-colors duration-300 shadow-md hover:shadow-lg mb-4"
                >
                    Enter
                </button>
                <button 
                    onClick={handleLogin} 
                    className="bg-green-500 hover:bg-green-600 text-white font-semibold text-lg px-8 py-3 rounded-md transition-colors duration-300 shadow-md hover:shadow-lg"
                >
                    Login
                </button>
            </div>
        </div>
    );
};

export default Index;
