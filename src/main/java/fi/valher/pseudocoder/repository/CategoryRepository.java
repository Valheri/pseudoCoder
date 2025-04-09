package fi.valher.pseudocoder.repository;

import org.springframework.data.repository.CrudRepository;

import fi.valher.pseudocoder.model.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
