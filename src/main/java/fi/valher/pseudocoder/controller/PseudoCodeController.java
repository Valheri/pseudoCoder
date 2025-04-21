package fi.valher.pseudocoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import jakarta.annotation.PostConstruct;

@CrossOrigin(origins = "http://localhost:5173") // Ensure this is present
@RestController
@RequestMapping("/api")
public class PseudoCodeController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PseudoCodeRepository pseudoCodeRepository;

    @Autowired
    private PseudoBlockRepository pseudoBlockRepository;

    // Initialize test data
    @PostConstruct
    public void initTestData() {
        // Create two distinct PseudoCodes (with auto-initialized categories)
        PseudoCode code1 = new PseudoCode("Code 1", null);
        PseudoCode code2 = new PseudoCode("Code 2", null);

        // Create distinct categories for Code 1
        Category cat1Code1 = new Category("UI Design", "blue", code1);
        Category cat2Code1 = new Category("Game Mechanics", "green", code1);
        code1.getCategories().add(cat1Code1);
        code1.getCategories().add(cat2Code1);

        // Create distinct categories for Code 2
        Category cat1Code2 = new Category("Physics", "purple", code2);
        Category cat2Code2 = new Category("Audio", "pink", code2);
        code2.getCategories().add(cat1Code2);
        code2.getCategories().add(cat2Code2);

        pseudoCodeRepository.save(code1);
        pseudoCodeRepository.save(code2);

        // Instead of adding pseudoBlocks directly on PseudoCode, add them to the corresponding Category
        PseudoBlock block1 = new PseudoBlock("Main Menu", "Design the main menu UI", cat1Code1, 1, "{}", null);
        PseudoBlock block2 = new PseudoBlock("Player Movement", "Implement basic player movement", cat2Code1, 2,
                "{\"speed\": \"5\"}", null);
        cat1Code1.getPseudoBlocks().add(block1);
        cat2Code1.getPseudoBlocks().add(block2);
        pseudoBlockRepository.save(block1);
        pseudoBlockRepository.save(block2);

        PseudoBlock block3 = new PseudoBlock("Enemy AI", "Implement enemy AI", cat1Code2, 1,
                "{\"aggressiveness\": \"high\"}", null);
        PseudoBlock block4 = new PseudoBlock("Sound Effects", "Add sound effects", cat2Code2, 2, "{\"volume\":\"80\"}",
                null);
        cat1Code2.getPseudoBlocks().add(block3);
        cat2Code2.getPseudoBlocks().add(block4);
        pseudoBlockRepository.save(block3);
        pseudoBlockRepository.save(block4);

        // Optional: Save categories if needed
        categoryRepository.save(cat1Code1);
        categoryRepository.save(cat2Code1);
        categoryRepository.save(cat1Code2);
        categoryRepository.save(cat2Code2);
    }

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
