package fi.valher.pseudocoder.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fi.valher.pseudocoder.model.PseudoCode;

public interface PseudoCodeRepository extends CrudRepository<PseudoCode, Long> {
    List<PseudoCode> findByName(String name);
}
