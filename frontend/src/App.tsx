import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { Route, Routes, useNavigate } from 'react-router-dom';
import Index from './components/Index';
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

const App: React.FC = () => {
    const [pseudoCodes, setPseudoCodes] = useState<PseudoCode[]>([]);
    const [selectedPseudoCode, setSelectedPseudoCode] = useState<PseudoCode | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        axios.get<PseudoCode[]>('/api/pseudoCodes')
            .then(response => {
                console.log('Response data:', response.data);
                setPseudoCodes(response.data);
            })
            .catch(error => console.error('Error fetching pseudoCodes:', error));
    }, []);

    const handleViewPseudoCode = (id: number) => {
        axios.get<PseudoCode>(`/api/pseudoCodes/${id}`)
            .then(response => {
                setSelectedPseudoCode(response.data);
                navigate(`/pseudoCodes/${id}`);
            })
            .catch(error => console.error('Error fetching pseudoCode:', error));
    };

    const handleAddPseudoCode = (name: string) => {
        axios.post('/api/pseudoCodes', { name })
            .then(() => {
                axios.get<PseudoCode[]>('/api/pseudoCodes')
                    .then(response => setPseudoCodes(response.data));
            })
            .catch(error => console.error('Error adding pseudoCode:', error));
    };

    const handleAddPseudoBlock = (pseudoCodeId: number, blockData: any) => {
        axios.post(`/api/pseudoCodes/${pseudoCodeId}/addPseudoBlock`, blockData)
            .then(() => {
                axios.get<PseudoCode>(`/api/pseudoCodes/${pseudoCodeId}`)
                    .then(response => setSelectedPseudoCode(response.data));
            })
            .catch(error => console.error('Error adding pseudoBlock:', error));
    };

    const handleEditPseudoBlock = (blockId: number, updatedBlock: any) => {
        axios.put(`/api/pseudoBlocks/${blockId}`, updatedBlock)
            .then(() => {
                if (selectedPseudoCode) {
                    axios.get<PseudoCode>(`/api/pseudoCodes/${selectedPseudoCode.id}`)
                        .then(response => setSelectedPseudoCode(response.data));
                }
            })
            .catch(error => console.error('Error editing pseudoBlock:', error));
    };

    const handleDeletePseudoBlock = (blockId: number) => {
        axios.delete(`/api/pseudoBlocks/${blockId}`)
            .then(() => {
                if (selectedPseudoCode) {
                    axios.get<PseudoCode>(`/api/pseudoCodes/${selectedPseudoCode.id}`)
                        .then(response => setSelectedPseudoCode(response.data));
                }
            })
            .catch(error => console.error('Error deleting pseudoBlock:', error));
    };

    const handleDeletePseudoCode = (id: number) => {
        axios.delete(`/api/pseudoCodes/${id}`)
            .then(() => {
                axios.get<PseudoCode[]>('/api/pseudoCodes')
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
            <Route path="/" element={<Index onEnter={handleBackToHome} />} />
            <Route
                path="/pseudoCodes"
                element={
                    <PseudoCodes
                        pseudoCodes={pseudoCodes}
                        onAddPseudoCode={handleAddPseudoCode}
                        onViewPseudoCode={handleViewPseudoCode}
                        onDeletePseudoCode={handleDeletePseudoCode}
                        onBack={handleBackToHome}
                    />
                }
            />
            <Route
                path="/pseudoCodes/:id"
                element={
                    selectedPseudoCode && (
                        <ViewPseudoCode
                            pseudoCode={selectedPseudoCode}
                            onAddPseudoBlock={handleAddPseudoBlock}
                            onEditPseudoBlock={handleEditPseudoBlock}
                            onDeletePseudoBlock={handleDeletePseudoBlock}
                            onBack={handleBackToPseudoCodes}
                            onUpdatePseudoCode={(updated) => setSelectedPseudoCode(updated)}
                        />
                    )
                }
            />
        </Routes>
    );
};

export default App;
