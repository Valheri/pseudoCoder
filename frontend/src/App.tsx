/// <reference types="node" />

import axios from 'axios';
import React, { JSX, useEffect, useState } from 'react';
import { Navigate, Route, Routes, useNavigate } from 'react-router-dom';
import Index from './components/Index';
import Login from './components/Login';
import PseudoCodes from './components/PseudoCodes';
import ViewPseudoCode from './components/ViewPseudoCode';

// Define types for frontend (adjust as needed)
interface PseudoBlock {
    id: number;
    name: string;
    description: string;
    blockOrder: number;
    parameters: string;
    output: string;
    category: { id: number; name?: string; colour: string };
}
interface Category {
    id: number;
    name: string;
    colour: string;
    pseudoBlocks?: PseudoBlock[];
}
interface PseudoCode {
    id: number;
    name: string;
    categories?: Category[];
}

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || ''; // Use environment variable or default to empty string

const App: React.FC = () => {
    const [pseudoCodes, setPseudoCodes] = useState<PseudoCode[]>([]);
    const [selectedPseudoCode, setSelectedPseudoCode] = useState<PseudoCode | null>(null);
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (isAuthenticated) {
            axios.get<PseudoCode[]>(`${API_BASE_URL}/api/pseudoCodes`)
                .then(response => {
                    // Handle response data appropriately (e.g., update state or log with a proper logging library)
                    setPseudoCodes(response.data);
                })
                .catch(error => console.error('Error fetching pseudoCodes:', error));
        }
    }, [isAuthenticated]);

    const handleLogin = (username: string, password: string) => {
        axios.post(`${API_BASE_URL}/api/login`, { username, password }, { withCredentials: true })
            .then(response => {
                console.log('Login successful:', response.data);
                setIsAuthenticated(true);
                navigate('/pseudoCodes');
            })
            .catch(error => {
                console.error('Login failed:', error.response ? error.response.data : error.message);
                alert('Login failed: ' + (error.response ? error.response.data : 'Network error'));
            });
    };

    const ProtectedRoute = ({ children }: { children: JSX.Element }) => {
        return isAuthenticated ? children : <Navigate to="/login" replace />;
    };

    const handleViewPseudoCode = (id: number) => {
        axios.get<PseudoCode>(`${API_BASE_URL}/api/pseudoCodes/${id}`)
            .then(response => {
                setSelectedPseudoCode(response.data);
                navigate(`/pseudoCodes/${id}`);
            })
            .catch(error => console.error('Error fetching pseudoCode:', error));
    };

    const handleAddPseudoCode = (name: string) => {
        if (!name.trim()) {
            alert("PseudoCode name cannot be empty.");
            return;
        }
        axios.post(`${API_BASE_URL}/api/pseudoCodes`, { name })
            .then(() => {
                axios.get<PseudoCode[]>(`${API_BASE_URL}/api/pseudoCodes`)
                    .then(response => setPseudoCodes(response.data));
            })
            .catch(error => console.error('Error adding pseudoCode:', error));
    };

    const handleAddPseudoBlock = (pseudoCodeId: number, blockData: any) => {
        if (!blockData || typeof blockData !== 'object' || !blockData.name || !blockData.description) {
            console.error('Invalid blockData:', blockData);
            alert('Invalid block data. Please ensure all required fields are filled.');
            return;
        }
        axios.post(`${API_BASE_URL}/api/pseudoCodes/${pseudoCodeId}/addPseudoBlock`, blockData)
            .then(() => {
                axios.get<PseudoCode>(`${API_BASE_URL}/api/pseudoCodes/${pseudoCodeId}`)
                    .then(response => setSelectedPseudoCode(response.data));
            })
            .catch(error => console.error('Error adding pseudoBlock:', error));
    };

    const handleEditPseudoBlock = (id: number, updatedBlock: any) => {
        // Adjusted to match the expected signature
        console.log(`Editing pseudo block ${id}`, updatedBlock);
    };

    const handleDeletePseudoBlock = (blockId: number) => {
        // Implement or mock this function to fix the error
        console.log(`Deleting pseudo block ${blockId}`);
    };

    const handleDeletePseudoCode = (id: number) => {
        axios.delete(`${API_BASE_URL}/api/pseudoCodes/${id}`)
            .then(() => {
                axios.get<PseudoCode[]>(`${API_BASE_URL}/api/pseudoCodes`)
                    .then(response => {
                        setPseudoCodes(response.data);
                        if (selectedPseudoCode && selectedPseudoCode.id === id) {
                            setSelectedPseudoCode(null);
                        }
                    });
            })
            .catch(error => console.error('Error deleting pseudoCode:', error));
    };

    const handleBackToHome = () => {
        navigate('/');
    };

    const handleBackToPseudoCodes = () => {
        navigate('/pseudoCodes');
    };

    return (
        <Routes>
            <Route path="/" element={<Index onEnter={() => navigate('/pseudoCodes')} />} />
            <Route path="/login" element={<Login onLogin={handleLogin} />} />
            <Route
                path="/pseudoCodes"
                element={
                    <ProtectedRoute>
                        <PseudoCodes
                            pseudoCodes={pseudoCodes}
                            onAddPseudoCode={handleAddPseudoCode}
                            onViewPseudoCode={handleViewPseudoCode}
                            onDeletePseudoCode={handleDeletePseudoCode}
                            onBack={handleBackToHome}
                        />
                    </ProtectedRoute>
                }
            />
            <Route
                path="/pseudoCodes/:id"
                element={
                    <ProtectedRoute>
                        {selectedPseudoCode ? (
                            <ViewPseudoCode
                                pseudoCode={selectedPseudoCode}
                                onAddPseudoBlock={handleAddPseudoBlock}
                                onEditPseudoBlock={handleEditPseudoBlock} // Updated to match the expected signature
                                onDeletePseudoBlock={handleDeletePseudoBlock}
                                onBack={handleBackToPseudoCodes}
                                onUpdatePseudoCode={(updated) => setSelectedPseudoCode(updated)}
                            />
                        ) : (
                            <div>Loading...</div>
                        )}
                    </ProtectedRoute>
                }
            />
            <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
    );
};

export default App;
