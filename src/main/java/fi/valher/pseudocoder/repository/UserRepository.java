package fi.valher.pseudocoder.repository;

import org.springframework.data.repository.CrudRepository;

import fi.valher.pseudocoder.model.AppUser;

public interface UserRepository extends CrudRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}