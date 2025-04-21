package fi.valher.pseudocoder.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@JsonIgnoreProperties({})
public class PseudoBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private int blockOrder;
    private String parameters;
    private String output;

    @ManyToOne
    @JsonIgnoreProperties("pseudoBlocks") // Exclude pseudoBlocks from the category when serializing
    private Category category;

    public PseudoBlock() {}

    public PseudoBlock(String name, String description, Category category, int blockOrder, String parameters, String output) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.blockOrder = blockOrder;
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

    public int getBlockOrder() {
        return blockOrder;
    }

    public void setBlockOrder(int blockOrder) {
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
        if (category == null || category.getId() == null) {
            throw new IllegalArgumentException("Category must be valid and have an ID.");
        }
        this.category = category;
    }
}
