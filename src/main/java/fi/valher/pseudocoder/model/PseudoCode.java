package fi.valher.pseudocoder.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class PseudoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToOne
    private AppUser user;

   

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pseudoCode")
    private List<PseudoBlock> pseudoBlocks;

    public PseudoCode() {}

    public PseudoCode(String name, AppUser user) {
        this.name = name;
        this.user = user;
       
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

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

   
    public List<PseudoBlock> getPseudoBlocks() {
        return pseudoBlocks;
    }

    public void setPseudoBlocks(List<PseudoBlock> pseudoBlocks) {
        this.pseudoBlocks = pseudoBlocks;
    }
}
