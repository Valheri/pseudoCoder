import React, { useState } from 'react';

interface PseudoCode {
    id: number;
    name: string;
}

interface PseudoCodesProps {
    pseudoCodes: PseudoCode[];
    onAddPseudoCode: (name: string) => void;
    onViewPseudoCode: (id: number) => void;
    onDeletePseudoCode: (id: number) => void;
    onBack: () => void;
}

const PseudoCodes: React.FC<PseudoCodesProps> = ({
    pseudoCodes,
    onAddPseudoCode,
    onViewPseudoCode,
    onDeletePseudoCode, // Added default fallback function
    onBack,
}) => {
    const [newPseudoCodeName, setNewPseudoCodeName] = useState('');

    const handleAddPseudoCode = () => {
        if (!newPseudoCodeName.trim()) {
            alert("PseudoCode name cannot be empty.");
            return;
        }
        onAddPseudoCode(newPseudoCodeName);
        setNewPseudoCodeName(''); // Clear the input field after adding
    };

    return (
        <div className="max-w-4xl mx-auto p-6 bg-white shadow-lg rounded-lg">
            <h1 className="text-3xl font-bold text-gray-800 mb-6">All PseudoCodes</h1>
            <div className="flex gap-2 mb-6">
                <input
                    type="text"
                    placeholder="Enter PseudoCode name"
                    value={newPseudoCodeName}
                    onChange={(e) => setNewPseudoCodeName(e.target.value)}
                    className="flex-grow px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                <button 
                    onClick={handleAddPseudoCode} 
                    className="bg-blue-500 hover:bg-blue-600 text-white font-medium px-4 py-2 rounded transition-colors duration-300"
                >
                    Add New PseudoCode
                </button>
            </div>
            <div className="overflow-x-auto">
                <table className="min-w-full bg-white border border-gray-200 rounded-lg overflow-hidden">
                    <thead className="bg-gray-100">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">ID</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">Name</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">Actions</th>
                        </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                        {pseudoCodes.map((code) => (
                            <tr key={code.id} className="hover:bg-gray-50">
                                <td className="px-6 py-4 whitespace-nowrap">{code.id}</td>
                                <td className="px-6 py-4 whitespace-nowrap">{code.name}</td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    <button 
                                        onClick={() => onViewPseudoCode(code.id)} 
                                        className="bg-green-500 hover:bg-green-600 text-white px-3 py-1 rounded text-sm transition-colors duration-300"
                                    >
                                        View
                                    </button>
                                    <button 
                                        onClick={() => onDeletePseudoCode(code.id)} 
                                        className="ml-2 bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded text-sm transition-colors duration-300"
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            <button 
                onClick={onBack} 
                className="mt-6 bg-gray-500 hover:bg-gray-600 text-white font-medium px-4 py-2 rounded transition-colors duration-300"
            >
                Back to Home
            </button>
        </div>
    );
};

export default PseudoCodes;
