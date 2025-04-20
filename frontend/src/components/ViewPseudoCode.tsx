import axios from 'axios';
import React, { useState } from 'react';
import EditPseudoBlock from './EditPseudoBlock';

// Updated interfaces – removed pseudoBlocks prop interface and use pseudoCode.categories instead
interface Category {
    id: number;
    name: string;
    colour: string;
    pseudoBlocks?: PseudoBlock[];
}
interface PseudoBlock {
    id: number;
    name: string;
    description: string;
    blockOrder: number;
    parameters: string;
    output: string;
    category: { id: number; colour: string; name?: string };
}
interface ViewPseudoCodeProps {
    pseudoCode: {
        id: number;
        name: string;
        categories?: Category[];
    };
    onAddPseudoBlock: (pseudoCodeId: number, blockData: any) => void;
    onEditPseudoBlock: (id: number, updatedBlock: any) => void;
    onDeletePseudoBlock: (id: number) => void;
    onBack: () => void;
    onUpdatePseudoCode: (pseudoCode: any) => void;
}

const ViewPseudoCode: React.FC<ViewPseudoCodeProps> = ({
    pseudoCode,
    onAddPseudoBlock,
    onEditPseudoBlock,
    onDeletePseudoBlock,
    onBack,
    onUpdatePseudoCode,
}) => {
    const [editingBlock, setEditingBlock] = useState<PseudoBlock | null>(null);
    const [isAdding, setIsAdding] = useState(false);
    const [selectedCategoryId, setSelectedCategoryId] = useState<number | null>(null);

    // Aggregate pseudoBlocks from all categories in pseudoCode
    // Map each block so that it gets the full category info.
    const aggregatedBlocks = pseudoCode.categories
        ? pseudoCode.categories.flatMap(cat =>
            (cat.pseudoBlocks || []).map(block => ({
                ...block,
                category: { ...cat }  // replace block.category with full category info from category object
            }))
        )
        : [];
    console.log("Aggregated pseudoBlocks:", aggregatedBlocks);  // Debug log

    // Filter blocks by selected category using the full category id
    const filteredBlocks = selectedCategoryId
        ? aggregatedBlocks.filter(block => block.category?.id === selectedCategoryId)
        : aggregatedBlocks;

    const handleSaveBlock = (blockData: any) => {
        if (isAdding) {
            const { id, ...newBlockData } = blockData;
            onAddPseudoBlock(pseudoCode.id, newBlockData);
        } else if (editingBlock) {
            onEditPseudoBlock(editingBlock.id, blockData);
        }
        setEditingBlock(null);
        setIsAdding(false);
    };

    const handleCancel = () => {
        setEditingBlock(null);
        setIsAdding(false);
    };

    const handleCreateCategory = () => {
        const name = prompt("Enter category name:");
        if (!name) return;
        const colour = prompt("Enter category colour (e.g., blue, #ff0000):");
        if (!colour) return;
        axios.post(`http://localhost:8080/api/pseudoCodes/${pseudoCode.id}/categories`, { name, colour })
            .then(() => {
                axios.get(`http://localhost:8080/api/pseudoCodes/${pseudoCode.id}`)
                    .then(response => {
                        onUpdatePseudoCode(response.data);
                    })
                    .catch(error => console.error("Error fetching updated pseudoCode:", error));
            })
            .catch(error => console.error("Error creating category:", error));
    };

    const handleDeleteCategory = (categoryId: number) => {
        if (!window.confirm("Are you sure you want to delete this category?")) return;
        axios.delete(`http://localhost:8080/api/categories/${categoryId}`)
            .then(() => {
                axios.get(`http://localhost:8080/api/pseudoCodes/${pseudoCode.id}`)
                    .then(response => onUpdatePseudoCode(response.data))
                    .catch(error => console.error("Error fetching updated pseudoCode:", error));
            })
            .catch(error => console.error("Error deleting category:", error));
    };

    return (
        <div className="max-w-6xl mx-auto p-6 bg-white shadow-lg rounded-lg">
            <h1 className="text-3xl font-bold text-gray-800 mb-6">PseudoCode: {pseudoCode.name}</h1>
            {editingBlock || isAdding ? (
                <EditPseudoBlock
                    pseudoBlock={
                        editingBlock || {
                            id: 0,
                            name: '',
                            description: '',
                            blockOrder: 0,
                            parameters: '',
                            output: '',
                            // Use first category's id if available; otherwise default to 0.
                            category: { id: (pseudoCode.categories && pseudoCode.categories.length > 0 ? pseudoCode.categories[0].id : 0) }
                        }
                    }
                    categories={pseudoCode.categories || []}
                    onSave={(blockData) => {
                        const payload = {
                            ...blockData,
                            category: { id: blockData.category.id }
                        };
                        handleSaveBlock(payload);
                    }}
                    onCancel={handleCancel}
                />
            ) : (
                <>
                    <button
                        onClick={() => setIsAdding(true)}
                        disabled={(pseudoCode.categories?.length || 0) === 0}
                        className={`mb-6 ${(pseudoCode.categories?.length || 0) === 0 ? 'opacity-50 cursor-not-allowed' : 'bg-blue-500 hover:bg-blue-600'} text-white font-medium px-4 py-2 rounded transition-colors duration-300`}
                    >
                        Add PseudoBlock
                    </button>

                    <div className="flex items-center mb-4 space-x-2">
                        <div className="mr-2 font-medium">Filter by category:</div>
                        <button
                            onClick={() => setSelectedCategoryId(null)}
                            className={`px-3 py-1 rounded text-sm transition-colors duration-300 ${selectedCategoryId === null ? 'bg-gray-700 text-white' : 'bg-gray-200 text-gray-700'}`}
                        >
                            All
                        </button>
                        {pseudoCode.categories?.map(category => (
                            <div key={category.id} className="flex items-center space-x-1">
                                <button
                                    onClick={() => setSelectedCategoryId(category.id)}
                                    className="px-3 py-1 rounded text-sm transition-colors duration-300"
                                    style={{
                                        backgroundColor: category.colour,
                                        color: 'white',
                                        border: selectedCategoryId === category.id ? '2px solid #000' : '1px solid #ccc'
                                    }}
                                >
                                    {category.name}
                                </button>
                                <button
                                    onClick={() => handleDeleteCategory(category.id)}
                                    className="px-1 py-1 bg-red-500 hover:bg-red-600 text-white rounded text-xs"
                                >
                                    X
                                </button>
                            </div>
                        ))}
                        <div className="flex-grow"></div>
                        <button
                            onClick={handleCreateCategory}
                            className="px-3 py-1 bg-green-500 hover:bg-green-600 text-white rounded text-sm transition-colors duration-300"
                        >
                            Create Category
                        </button>
                    </div>

                    <h2 className="text-xl font-semibold text-gray-700 mb-4">PseudoBlocks</h2>
                    <div className="overflow-x-auto">
                        <table className="min-w-full bg-white border border-gray-200 rounded-lg overflow-hidden">
                            <thead className="bg-gray-100">
                                <tr>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">Category</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">Name</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">Description</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">Order</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">Parameters</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">Output</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">Actions</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-200">
                                {filteredBlocks.length > 0 ? (
                                    filteredBlocks.map((block) => (
                                        <tr key={block.id} className="hover:bg-gray-50">
                                            <td className="px-6 py-4 whitespace-nowrap" style={{ backgroundColor: block.category.colour }}>
                                                {block.category.name}
                                            </td>
                                            <td className="px-6 py-4 whitespace-nowrap">{block.name}</td>
                                            <td className="px-6 py-4 whitespace-nowrap">{block.description}</td>
                                            <td className="px-6 py-4 whitespace-nowrap">{block.blockOrder}</td>
                                            <td className="px-6 py-4 whitespace-nowrap">{block.parameters}</td>
                                            <td className="px-6 py-4 whitespace-nowrap">{block.output}</td>
                                            <td className="px-6 py-4 whitespace-nowrap">
                                                <button
                                                    onClick={() => setEditingBlock(block)}
                                                    className="bg-amber-500 hover:bg-amber-600 text-white px-3 py-1 rounded text-sm transition-colors duration-300"
                                                >
                                                    Edit
                                                </button>
                                                <button
                                                    onClick={() => onDeletePseudoBlock(block.id)}
                                                    className="ml-2 bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded text-sm transition-colors duration-300"
                                                >
                                                    Delete
                                                </button>
                                            </td>
                                        </tr>
                                    ))
                                ) : (
                                    <tr>
                                        <td colSpan={7} className="px-6 py-4 text-center text-gray-500">No PseudoBlocks available.</td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                    <button
                        onClick={onBack}
                        className="mt-6 bg-gray-500 hover:bg-gray-600 text-white font-medium px-4 py-2 rounded transition-colors duration-300"
                    >
                        Back to All PseudoCodes
                    </button>
                </>
            )}
        </div>
    );
};

export default ViewPseudoCode;
