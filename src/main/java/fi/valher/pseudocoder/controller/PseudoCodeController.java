package fi.valher.pseudocoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fi.valher.pseudocoder.model.Category;
import fi.valher.pseudocoder.model.PseudoBlock;
import fi.valher.pseudocoder.model.PseudoCode;
import fi.valher.pseudocoder.repository.CategoryRepository;
import fi.valher.pseudocoder.repository.PseudoBlockRepository;
import fi.valher.pseudocoder.repository.PseudoCodeRepository;
import jakarta.annotation.PostConstruct;

@Controller
public class PseudoCodeController  {


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PseudoCodeRepository pseudoCodeRepository;

    @Autowired
    private PseudoBlockRepository pseudoBlockRepository;

    @GetMapping({"/index", "/"})
    public String index(Model model) {
        model.addAttribute("message", "Welcome to the pseudoCoder!");
        return "index";
    }

   
    @GetMapping("/login")
    public String login() {
        return "login";
    }

   
    // Initialize test data
    @PostConstruct
    public void initTestData() {
        // Create categories
        Category category1 = new Category("UI Design", "blue");
        Category category2 = new Category("Game Mechanics", "green");
        Category category3 = new Category("Level Design", "orange");
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        // Create PseudoCodes
        PseudoCode code1 = new PseudoCode("Code 1", null);
        PseudoCode code2 = new PseudoCode("Code 2", null);
        pseudoCodeRepository.save(code1);
        pseudoCodeRepository.save(code2);

        // Add blocks to Code 1
        PseudoBlock block1 = new PseudoBlock("Main Menu", "Design the main menu UI", category1, code1, 1, "{}", null);
        PseudoBlock block2 = new PseudoBlock("Player Movement", "Implement basic player movement mechanics", category2, code1, 2, "{\"speed\": \"5\"}", null);
        PseudoBlock block3 = new PseudoBlock("Level 1 Layout", "Create the layout for the first level", category3, code1, 3, "{\"difficulty\": \"easy\"}", null);
        pseudoBlockRepository.save(block1);
        pseudoBlockRepository.save(block2);
        pseudoBlockRepository.save(block3);

        // Add blocks to Code 2
        PseudoBlock block4 = new PseudoBlock("Settings Menu", "Design the settings menu UI", category1, code2, 1, "{}", null);
        PseudoBlock block5 = new PseudoBlock("Enemy AI", "Implement basic enemy AI mechanics", category2, code2, 2, "{\"aggressiveness\": \"high\"}", null);
        PseudoBlock block6 = new PseudoBlock("Level 2 Layout", "Create the layout for the second level", category3, code2, 3, "{\"difficulty\": \"medium\"}", null);
        pseudoBlockRepository.save(block4);
        pseudoBlockRepository.save(block5);
        pseudoBlockRepository.save(block6);
    }

    @GetMapping("/pseudoCodes")
public String viewAllPseudoCodes(Model model) {
    model.addAttribute("pseudoCodes", pseudoCodeRepository.findAll());
    return "pseudoCodes"; // Use a new template for the list of pseudo codes
}

    @GetMapping("/pseudoCodes/{id}")
    public String viewPseudoCode(@PathVariable Long id, Model model) {
        PseudoCode pseudoCode = pseudoCodeRepository.findById(id).orElseThrow();
        model.addAttribute("pseudoCode", pseudoCode);
        model.addAttribute("pseudoBlocks", pseudoBlockRepository.findByPseudoCode(pseudoCode)); // Correctly handle the list of blocks
        return "viewPseudoCode"; // Ensure this template is updated to handle single PseudoCode view
    }

    @GetMapping("/pseudoCodes/{id}/addPseudoBlock")
    public String showAddPseudoBlockForm(@PathVariable Long id, Model model) {
        PseudoCode pseudoCode = pseudoCodeRepository.findById(id).orElseThrow();
        model.addAttribute("pseudoCode", pseudoCode);
        model.addAttribute("pseudoBlock", new PseudoBlock());
        model.addAttribute("categories", categoryRepository.findAll()); // Add categories to the model
        return "addPseudoBlock";
    }

    @PostMapping("/pseudoCodes/{id}/addPseudoBlock")
    public String addPseudoBlock(@PathVariable Long id, PseudoBlock pseudoBlock) {
        PseudoCode pseudoCode = pseudoCodeRepository.findById(id).orElseThrow();
        pseudoBlock.setId(null); // Ensure a new block is created
        pseudoBlock.setPseudoCode(pseudoCode);
        pseudoBlockRepository.save(pseudoBlock);
        return "redirect:/pseudoCodes/" + id;
    }

    @GetMapping("/pseudoCodes/add")
    public String showAddPseudoCodeForm(Model model) {
        model.addAttribute("pseudoCode", new PseudoCode());
        return "addPseudoCode"; // Render a form for adding a new PseudoCode
    }

    @PostMapping("/pseudoCodes/add")
    public String addPseudoCode(PseudoCode pseudoCode) {
        pseudoCodeRepository.save(pseudoCode);
        return "redirect:/pseudoCodes";
    }

    @PostMapping("/pseudoBlocks/update")
    public String updatePseudoBlock(PseudoBlock pseudoBlock) {
        PseudoBlock existingBlock = pseudoBlockRepository.findById(pseudoBlock.getId()).orElseThrow();
        existingBlock.setName(pseudoBlock.getName());
        existingBlock.setDescription(pseudoBlock.getDescription());
        existingBlock.setBlockOrder(pseudoBlock.getBlockOrder());
        existingBlock.setParameters(pseudoBlock.getParameters());
        existingBlock.setOutput(pseudoBlock.getOutput());
        existingBlock.setCategory(categoryRepository.findById(pseudoBlock.getCategory().getId()).orElseThrow());
        pseudoBlockRepository.save(existingBlock);
        return "redirect:/pseudoCodes/" + existingBlock.getPseudoCode().getId();
    }

    @GetMapping("/pseudoBlocks/edit/{id}")
    public String editPseudoBlock(@PathVariable Long id, Model model) {
        PseudoBlock pseudoBlock = pseudoBlockRepository.findById(id).orElseThrow();
        model.addAttribute("pseudoBlock", pseudoBlock);
        model.addAttribute("categories", categoryRepository.findAll()); // Add categories to the model
        return "editPseudoBlock";
    }
}
