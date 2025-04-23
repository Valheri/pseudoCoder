package fi.valher.pseudocoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.valher.pseudocoder.model.Category;
import fi.valher.pseudocoder.model.PseudoBlock;
import fi.valher.pseudocoder.model.PseudoCode;
import fi.valher.pseudocoder.repository.CategoryRepository;
import fi.valher.pseudocoder.repository.PseudoBlockRepository;
import fi.valher.pseudocoder.repository.PseudoCodeRepository;

@RestController
@RequestMapping("/api")
public class PseudoCodeController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PseudoCodeRepository pseudoCodeRepository;

    @Autowired
    private PseudoBlockRepository pseudoBlockRepository;

   

    @PutMapping(path = "/pseudoBlocks/{id}", consumes = "application/json")
    public void editPseudoBlock(@PathVariable Long id, @RequestBody PseudoBlock updatedBlock) {
        PseudoBlock existingBlock = pseudoBlockRepository.findById(id).orElseThrow();
        existingBlock.setName(updatedBlock.getName());
        existingBlock.setDescription(updatedBlock.getDescription());
        existingBlock.setBlockOrder(updatedBlock.getBlockOrder());
        existingBlock.setParameters(updatedBlock.getParameters());
        existingBlock.setOutput(updatedBlock.getOutput());
        existingBlock.setCategory(categoryRepository.findById(updatedBlock.getCategory().getId()).orElseThrow());
        pseudoBlockRepository.save(existingBlock);
    }

    @GetMapping("/pseudoCodes")
    public List<PseudoCode> getAllPseudoCodes() {
        List<PseudoCode> pseudoCodes = (List<PseudoCode>) pseudoCodeRepository.findAll();
        return pseudoCodes;
    }

    @GetMapping("/pseudoCodes/{id}")
    public PseudoCode getPseudoCodeById(@PathVariable Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id is not provided or is invalid.");
        }

        return pseudoCodeRepository.findById(id)
                .map(pseudoCode -> {
                    // Force initialization of pseudoBlocks via each category.
                    pseudoCode.getCategories().forEach(cat -> {
                        if (cat.getPseudoBlocks() != null) {
                            cat.getPseudoBlocks().size();
                        }
                    });
                    return pseudoCode;
                })
                .orElseThrow(() -> new IllegalArgumentException("PseudoCode with id " + id + " could not be found"));
    }

    @PostMapping("/pseudoCodes/{id}/addPseudoBlock")
    public PseudoBlock addPseudoBlockToPseudoCode(@PathVariable Long id, @RequestBody PseudoBlock pseudoBlock) {
        System.out.println("Received PseudoBlock: " + pseudoBlock); // Log the incoming payload

        if (id == null) {
            throw new IllegalArgumentException("PseudoCode ID is not provided or is invalid.");
        }

        if (pseudoBlock.getName() == null || pseudoBlock.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("PseudoBlock name cannot be null or empty.");
        }

        if (pseudoBlock.getCategory() == null || pseudoBlock.getCategory().getId() == null) {
            throw new IllegalArgumentException("PseudoBlock category must be provided.");
        }

        // Retrieve the category (which already belongs to a PseudoCode)
        Category category = categoryRepository.findById(pseudoBlock.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Category with id " + pseudoBlock.getCategory().getId() + " could not be found"));

        pseudoBlock.setCategory(category);
        // Do not set pseudoCode on the block; it is now managed via the Category's pseudoBlocks
        PseudoBlock savedBlock = pseudoBlockRepository.save(pseudoBlock);
        // Optionally add the block to the category's pseudoBlocks list if not automatically updated
        category.getPseudoBlocks().add(savedBlock);
        categoryRepository.save(category);
        return savedBlock;
    }

    @PostMapping("/pseudoCodes")
    public PseudoCode addPseudoCode(@RequestBody PseudoCode pseudoCode) {
        if (pseudoCode.getName() == null || pseudoCode.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("PseudoCode name cannot be null or empty.");
        }
        return pseudoCodeRepository.save(pseudoCode);
    }

    @PostMapping("/pseudoCodes/{id}/categories")
    public Category createCategory(@PathVariable Long id, @RequestBody Category newCategory) {
        PseudoCode pseudoCode = pseudoCodeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PseudoCode with id " + id + " not found"));
        newCategory.setPseudoCode(pseudoCode);
        return categoryRepository.save(newCategory);
    }

    // UNUSED: This endpoint is not directly used by the frontend.
    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        List<Category> categories = (List<Category>) categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new IllegalArgumentException("No categories found.");
        }
        return categories;
    }

    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable Long id) {
        if (id == null || !categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category with id " + id + " could not be found");
        }
        categoryRepository.deleteById(id);
    }

    @DeleteMapping("/pseudoBlocks/{id}")
    public void deletePseudoBlock(@PathVariable Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id is not provided or is invalid.");
        }

        if (!pseudoBlockRepository.existsById(id)) {
            throw new IllegalArgumentException("PseudoBlock with id " + id + " could not be found");
        }

        pseudoBlockRepository.deleteById(id);
    }

    @DeleteMapping("/pseudoCodes/{id}")
    public void deletePseudoCode(@PathVariable Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id is not provided or is invalid.");
        }
        if (!pseudoCodeRepository.existsById(id)) {
            throw new IllegalArgumentException("PseudoCode with id " + id + " could not be found");
        }
        pseudoCodeRepository.deleteById(id);
    }
}
