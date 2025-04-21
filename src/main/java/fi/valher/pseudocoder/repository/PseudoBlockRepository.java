package fi.valher.pseudocoder.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fi.valher.pseudocoder.model.PseudoBlock;
import fi.valher.pseudocoder.model.PseudoCode;

public interface PseudoBlockRepository extends CrudRepository<PseudoBlock, Long> {
    List<PseudoBlock> findByName(String name);

    List<PseudoBlock> findByCategory_PseudoCode(PseudoCode pseudoCode);
}
