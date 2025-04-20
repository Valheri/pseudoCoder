import React, { useState } from 'react';

interface Category {
    id: number;
    name: string;
}

interface EditPseudoBlockProps {
    pseudoBlock: {
        id: number;
        name: string;
        description: string;
        blockOrder: number;
        parameters: string;
        output: string;
        category: { id: number };
    };
    categories: Category[];
    onSave: (updatedBlock: any) => void;
    onCancel: () => void;
}

const EditPseudoBlock: React.FC<EditPseudoBlockProps> = ({ pseudoBlock, categories, onSave, onCancel }) => {
    const [formData, setFormData] = useState({
        ...pseudoBlock,
        name: pseudoBlock.name || '',
        description: pseudoBlock.description || '',
        blockOrder: pseudoBlock.blockOrder || 0,
        parameters: pseudoBlock.parameters || '',
        output: pseudoBlock.output || '',
        category: pseudoBlock.category || { id: 1 }, // Default category ID
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        if (name === 'category.id') {
            setFormData({ ...formData, category: { id: parseInt(value, 10) } }); // Update category ID
        } else {
            setFormData({ ...formData, [name]: value });
        }
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        const { id, ...formDataWithoutId } = formData; // Exclude the id field
        onSave(formDataWithoutId);
    };

    return (
        <div className="bg-white rounded-lg shadow-md p-6">
            <h2 className="text-2xl font-bold mb-6 text-gray-800">
                {pseudoBlock.id ? 'Edit PseudoBlock' : 'Add New PseudoBlock'}
            </h2>
            
            <form onSubmit={handleSubmit}>
                <div className="mb-4">
                    <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
                        Name:
                    </label>
                    <input
                        type="text"
                        id="name"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>
                
                <div className="mb-4">
                    <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">
                        Description:
                    </label>
                    <textarea
                        id="description"
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        required
                        rows={4}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>
                
                <div className="mb-4">
                    <label htmlFor="blockOrder" className="block text-sm font-medium text-gray-700 mb-1">
                        Order:
                    </label>
                    <input
                        type="number"
                        id="blockOrder"
                        name="blockOrder"
                        value={formData.blockOrder}
                        onChange={handleChange}
                        required
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>
                
                <div className="mb-4">
                    <label htmlFor="parameters" className="block text-sm font-medium text-gray-700 mb-1">
                        Parameters:
                    </label>
                    <textarea
                        id="parameters"
                        name="parameters"
                        value={formData.parameters}
                        onChange={handleChange}
                        rows={3}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>
                
                <div className="mb-4">
                    <label htmlFor="output" className="block text-sm font-medium text-gray-700 mb-1">
                        Output:
                    </label>
                    <input
                        type="text"
                        id="output"
                        name="output"
                        value={formData.output}
                        onChange={handleChange}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>
                
                <div className="mb-6">
                    <label htmlFor="category" className="block text-sm font-medium text-gray-700 mb-1">
                        Category:
                    </label>
                    <select
                        id="category"
                        name="category.id"
                        value={formData.category.id}
                        onChange={handleChange}
                        required
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                        {categories.map((category) => (
                            <option key={category.id} value={category.id}>
                                {category.name}
                            </option>
                        ))}
                    </select>
                </div>
                
                <div className="flex space-x-4">
                    <button 
                        type="submit"
                        className="bg-green-500 hover:bg-green-600 text-white font-medium px-6 py-2 rounded transition-colors duration-300"
                    >
                        Save
                    </button>
                    <button 
                        type="button" 
                        onClick={onCancel}
                        className="bg-gray-500 hover:bg-gray-600 text-white font-medium px-6 py-2 rounded transition-colors duration-300"
                    >
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    );
};

export default EditPseudoBlock;
