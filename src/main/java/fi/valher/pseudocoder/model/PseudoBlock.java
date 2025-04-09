package fi.valher.pseudocoder.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class PseudoBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private int blockOrder; // Renamed from 'order'
    private String parameters;
    private String output;

    @ManyToOne
    private Category category;

    @ManyToOne
    private PseudoCode pseudoCode;

    public PseudoBlock() {}

    public PseudoBlock(String name, String description, Category category, PseudoCode pseudoCode, int blockOrder, String parameters, String output) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.pseudoCode = pseudoCode;
        this.blockOrder = blockOrder; // Updated reference
        this.parameters = parameters;
        this.output = output;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBlockOrder() { // Renamed getter
        return blockOrder;
    }

    public void setBlockOrder(int blockOrder) { // Renamed setter
        this.blockOrder = blockOrder;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public PseudoCode getPseudoCode() {
        return pseudoCode;
    }

    public void setPseudoCode(PseudoCode pseudoCode) {
        if (pseudoCode == null || pseudoCode.getId() == null) {
            throw new IllegalArgumentException("Invalid code ID");
        }
        this.pseudoCode = pseudoCode;
    }
}
