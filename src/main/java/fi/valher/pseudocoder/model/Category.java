package fi.valher.pseudocoder.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String colour;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private List<PseudoBlock> pseudoBlocks;  // now this will be serialized

    @ManyToOne
    @JsonBackReference // Prevents recursion by ignoring this field during serialization
    private PseudoCode pseudoCode;

    public Category() {
        // Initialize pseudoBlocks for safe adds
        this.pseudoBlocks = new ArrayList<>();
    }

    public Category(String name) {
        this.name = name;
        this.pseudoBlocks = new ArrayList<>();
    }

    public Category(String name, String colour) {
        this.name = name;
        this.colour = colour;
        this.pseudoBlocks = new ArrayList<>();
    }

    public Category(String name, String colour, PseudoCode pseudoCode) {
        this.name = name;
        this.colour = colour;
        this.pseudoCode = pseudoCode;
        this.pseudoBlocks = new ArrayList<>();
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

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public List<PseudoBlock> getPseudoBlocks() {
        return pseudoBlocks;
    }

    public void setPseudoBlocks(List<PseudoBlock> pseudoBlocks) {
        this.pseudoBlocks = pseudoBlocks;
    }

    public PseudoCode getPseudoCode() {
        return pseudoCode;
    }

    public void setPseudoCode(PseudoCode pseudoCode) {
        this.pseudoCode = pseudoCode;
    }
}
